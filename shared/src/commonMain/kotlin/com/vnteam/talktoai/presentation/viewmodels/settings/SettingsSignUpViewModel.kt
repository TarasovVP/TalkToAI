package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.data.network.getDataOrNull
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.usecase.execute
import com.vnteam.talktoai.presentation.uistates.SettingsSignUpUIState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithGoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.FetchProvidersForEmailUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleSignInUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.GetChatsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.GetMessagesUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.InsertRemoteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.UpdateRemoteUserUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsSignUpViewModel(
    private val networkState: NetworkState,
    private val fetchProvidersForEmailUseCase: FetchProvidersForEmailUseCase,
    private val createUserWithGoogleUseCase: CreateUserWithGoogleUseCase,
    private val createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase,
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val getChatsUseCase: GetChatsUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val insertRemoteUserUseCase: InsertRemoteUserUseCase,
    private val updateRemoteUserUseCase: UpdateRemoteUserUseCase,
    private val googleUseCase: GoogleSignInUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SettingsSignUpUIState())
    val uiState = _uiState.asStateFlow()

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        launchWithResult {
            fetchProvidersForEmailUseCase.execute(email).onSuccess { result ->
                when {
                    result.isNullOrEmpty().not() -> updateUIState(SettingsSignUpUIState(accountExist = idToken.orEmpty()))
                    idToken.isNullOrEmpty() -> updateUIState(SettingsSignUpUIState(createEmailAccount = true))
                    else -> updateUIState(SettingsSignUpUIState(createGoogleAccount = idToken))
                }
            }
        }
    }

    fun createUserWithGoogle(idToken: String, isExistUser: Boolean) {
        launchWithResult {
            googleUseCase.execute(idToken)
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        launchWithResult {
            createUserWithEmailAndPasswordUseCase.execute(Pair(email, password)).onSuccess {
                updateUIState(SettingsSignUpUIState(successAuthorisation = false))
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        launchWithResult {
            signInWithEmailAndPasswordUseCase.execute(Pair(email, password)).onSuccess {
                updateUIState(SettingsSignUpUIState(successAuthorisation = true))
            }

        }
    }

    fun googleSign() {
        val someString = "someString"
        launchWithResult {
            googleUseCase.execute(someString)
        }
    }

    fun createRemoteUser(isExistUser: Boolean) {
        launchWithErrorHandling {
            val chats = getChatsUseCase.execute().getDataOrNull()
            val messages = getMessagesUseCase.execute().getDataOrNull()
            val remoteUser = RemoteUser().apply {
                this.chats.addAll(chats.orEmpty())
                this.messages.addAll(messages.orEmpty())
            }
            updateUIState(SettingsSignUpUIState(remoteUser = Pair(isExistUser, remoteUser)))
        }
    }

    fun insertRemoteCurrentUser(remoteUser: RemoteUser) {
        launchWithNetworkCheck(networkState) {
            insertRemoteUserUseCase.execute(remoteUser).onSuccess {
                updateUIState(SettingsSignUpUIState(successRemoteUser = true))
            }

        }
    }

    fun updateRemoteCurrentUser(remoteUser: RemoteUser) {
        launchWithNetworkCheck(networkState) {
            updateRemoteUserUseCase.execute(remoteUser).onSuccess {
                updateUIState(SettingsSignUpUIState(successRemoteUser = true))
            }
        }
    }

    private fun updateUIState(newUIState: SettingsSignUpUIState) {
        _uiState.value = newUIState
    }
}