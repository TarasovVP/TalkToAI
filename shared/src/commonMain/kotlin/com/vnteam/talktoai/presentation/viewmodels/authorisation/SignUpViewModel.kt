package com.vnteam.talktoai.presentation.viewmodels.authorisation

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.dateToMilliseconds
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.usecase.execute
import com.vnteam.talktoai.presentation.uistates.SignUpUIState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithGoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.FetchProvidersForEmailUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleSignInUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.InsertChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.InsertMessageUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.InsertRemoteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.IdTokenUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Clock

class SignUpViewModel(
    private val networkState: NetworkState,
    private val fetchProvidersForEmailUseCase: FetchProvidersForEmailUseCase,
    private val createUserWithGoogleUseCase: CreateUserWithGoogleUseCase,
    private val createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase,
    private val insertRemoteUserUseCase: InsertRemoteUserUseCase,
    private val googleUseCase: GoogleSignInUseCase,
    private val idTokenUseCase: IdTokenUseCase,
    private val userEmailUseCase: UserEmailUseCase,
    private val onboardingUseCase: OnboardingUseCase,
    private val insertChatUseCase: InsertChatUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState: StateFlow<SignUpUIState> = _uiState.asStateFlow()

    private var pendingIdToken: String = String.EMPTY
    private var pendingEmail: String = String.EMPTY

    fun fetchProvidersForEmailUseCase(idToken: String? = null) {
        launchWithResult {
            fetchProvidersForEmailUseCase.execute().onSuccess { result ->
                when {
                    result.isNullOrEmpty()
                        .not() -> updateUIState(SignUpUIState(accountExist = true))

                    idToken.isNullOrEmpty() -> updateUIState(SignUpUIState(createEmailAccount = true))
                    else -> updateUIState(SignUpUIState(createGoogleAccount = idToken))
                }
            }
        }
    }

    fun createUserWithGoogle(idToken: String) {
        launchWithResult {
            googleUseCase.execute(idToken)
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        launchWithErrorHandling {
            when (val result = createUserWithEmailAndPasswordUseCase.execute(Pair(email, password))) {
                is com.vnteam.talktoai.data.network.Result.Success -> {
                    hideProgress()
                    onboardingUseCase.set(true)
                    pendingIdToken = result.data?.idToken.orEmpty()
                    pendingEmail = result.data?.email.orEmpty()
                    updateUIState(SignUpUIState(successSignUp = true))
                }
                is com.vnteam.talktoai.data.network.Result.Failure -> onError(Exception(result.errorMessage))
                is com.vnteam.talktoai.data.network.Result.Loading -> showProgress()
            }
        }
    }

    fun insertRemoteUser(remoteUser: RemoteUser, welcomeChatName: String, welcomeMessage: String) {
        launchWithErrorHandling {
            val chatId = Clock.System.now().dateToMilliseconds()
            insertChatUseCase.execute(
                Chat(id = chatId, name = welcomeChatName, updated = chatId, listOrder = 1)
            )
            insertMessageUseCase.execute(
                Message(
                    chatId = chatId,
                    author = Constants.MESSAGE_ROLE_CHAT_GPT,
                    message = welcomeMessage,
                    status = MessageStatus.SUCCESS,
                    updatedAt = chatId
                )
            )
            idTokenUseCase.set(pendingIdToken)
            userEmailUseCase.set(pendingEmail)
            updateUIState(SignUpUIState(createCurrentUser = true))
            if (networkState.isNetworkAvailable()) {
                insertRemoteUserUseCase.execute(remoteUser).firstOrNull()
            }
        }
    }

    private fun updateUIState(newUIState: SignUpUIState) {
        _uiState.value = newUIState
    }

    fun googleSignOut() {
        //googleUseCase.googleSignOut()
    }

    fun googleSignIn() {
        val someString = "someString"
        launchWithResult {
            googleUseCase.execute(someString)
        }
    }
}
