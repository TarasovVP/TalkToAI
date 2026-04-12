package com.vnteam.talktoai.presentation.viewmodels.authorisation

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.usecase.execute
import com.vnteam.talktoai.presentation.uistates.SignUpUIState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithGoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.FetchProvidersForEmailUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleSignInUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.InsertRemoteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.IdTokenUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState: StateFlow<SignUpUIState> = _uiState.asStateFlow()

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
                    idTokenUseCase.set(result.data?.idToken.orEmpty())
                    userEmailUseCase.set(result.data?.email.orEmpty())
                    updateUIState(SignUpUIState(successSignUp = true))
                }
                is com.vnteam.talktoai.data.network.Result.Failure -> onError(Exception(result.errorMessage))
                is com.vnteam.talktoai.data.network.Result.Loading -> showProgress()
            }
        }
    }

    fun insertRemoteUser(remoteUser: RemoteUser) {
        launchWithNetworkCheck(networkState) {
            insertRemoteUserUseCase.execute(remoteUser).onSuccess {
                updateUIState(SignUpUIState(createCurrentUser = true))
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