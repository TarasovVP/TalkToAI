package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.getDataOrNull
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.usecase.SettingsSignUpUseCase
import com.vnteam.talktoai.presentation.uistates.SettingsSignUpUIState
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsSignUpViewModel(
    private val settingsSignUpUseCase: SettingsSignUpUseCase,
    private val networkState: NetworkState,
    //val googleSignInClient: GoogleSignInClient,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SettingsSignUpUIState())
    val uiState = _uiState.asStateFlow()

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.fetchSignInMethodsForEmail(email).onSuccess { result ->
                when {
                    result.isNullOrEmpty().not() -> updateUIState(SettingsSignUpUIState(accountExist = idToken.orEmpty()))
                    idToken.isNullOrEmpty() -> updateUIState(SettingsSignUpUIState(createEmailAccount = true))
                    else -> updateUIState(SettingsSignUpUIState(createGoogleAccount = idToken))
                }
            }
        }
    }

    fun createUserWithGoogle(idToken: String, isExistUser: Boolean) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.createUserWithGoogle(idToken).onSuccess {
                updateUIState(SettingsSignUpUIState(successAuthorisation = isExistUser))
            }
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.createUserWithEmailAndPassword(email, password).onSuccess {
                updateUIState(SettingsSignUpUIState(successAuthorisation = false))
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.signInWithEmailAndPassword(email, password).onSuccess {
                updateUIState(SettingsSignUpUIState(successAuthorisation = true))
            }

        }
    }

    fun createRemoteUser(isExistUser: Boolean) {
        launchWithErrorHandling {
            val chats = settingsSignUpUseCase.getChats().getDataOrNull()
            val messages = settingsSignUpUseCase.getMessages().getDataOrNull()
            val remoteUser = RemoteUser().apply {
                this.chats.addAll(chats.orEmpty())
                this.messages.addAll(messages.orEmpty())
            }
            updateUIState(SettingsSignUpUIState(remoteUser = Pair(isExistUser, remoteUser)))
        }
    }

    fun insertRemoteCurrentUser(remoteUser: RemoteUser) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.insertRemoteCurrentUser(remoteUser).onSuccess {
                updateUIState(SettingsSignUpUIState(successRemoteUser = true))
            }

        }
    }

    fun updateRemoteCurrentUser(remoteUser: RemoteUser) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.updateRemoteCurrentUser(remoteUser).onSuccess {
                updateUIState(SettingsSignUpUIState(successRemoteUser = true))
            }
        }
    }

    fun googleSign() {
        settingsSignUpUseCase.googleSign()
    }

    private fun updateUIState(newUIState: SettingsSignUpUIState) {
        _uiState.value = newUIState
    }
}