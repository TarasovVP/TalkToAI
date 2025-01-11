package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.usecase.SignUpUseCase
import com.vnteam.talktoai.presentation.SignUpUIState
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val networkState: NetworkState
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState: StateFlow<SignUpUIState> = _uiState.asStateFlow()

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        launchWithNetworkCheck(networkState) {
            signUpUseCase.fetchSignInMethodsForEmail(email).onSuccess { result ->
                when {
                    result.isNullOrEmpty().not() -> updateUIState(SignUpUIState(accountExist = true))
                    idToken.isNullOrEmpty() -> updateUIState(SignUpUIState(createEmailAccount = true))
                    else -> updateUIState(SignUpUIState(createGoogleAccount = idToken))
                }
            }
        }
    }

    fun createUserWithGoogle(idToken: String) {
        launchWithNetworkCheck(networkState) {
            signUpUseCase.createUserWithGoogle(idToken).onSuccess {
                updateUIState(SignUpUIState(successSignUp = true))
            }
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        launchWithNetworkCheck(networkState) {
            signUpUseCase.createUserWithEmailAndPassword(email, password).onSuccess {
                updateUIState(SignUpUIState(successSignUp = true))
            }
        }
    }

    fun insertRemoteUser(remoteUser: RemoteUser) {
        launchWithNetworkCheck(networkState) {
            signUpUseCase.insertRemoteUser(remoteUser).onSuccess {
                updateUIState(SignUpUIState(createCurrentUser = true))
            }
        }
    }

    private fun updateUIState(newUIState: SignUpUIState) {
        _uiState.value = newUIState
    }

    fun googleSignOut() {
        signUpUseCase.googleSignOut()
    }

    fun googleSignIn() {
        signUpUseCase.googleSignIn()
    }
}