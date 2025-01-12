package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.LoginUseCase
import com.vnteam.talktoai.presentation.uistates.LoginUIState
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun sendPasswordResetEmail(email: String) {
        launchWithNetworkCheck(networkState = networkState) {
            loginUseCase.sendPasswordResetEmail(email).onSuccess {
                updateUIState(LoginUIState(successPasswordReset = true))
            }
        }
    }

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        launchWithNetworkCheck(networkState = networkState) {
            loginUseCase.fetchSignInMethodsForEmail(email).onSuccess { data ->
                when {
                    data.isNullOrEmpty() -> updateUIState(LoginUIState(isAccountExist = true))
                    idToken.isNullOrEmpty() -> updateUIState(LoginUIState(isEmailAccountExist = true))
                    else -> updateUIState(LoginUIState(isGoogleAccountExist = idToken))
                }
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        launchWithNetworkCheck(networkState = networkState) {
            loginUseCase.signInWithEmailAndPassword(email, password).onSuccess {
                updateUIState(LoginUIState(successSignIn = true))
            }
        }
    }

    fun signInAuthWithGoogle(idToken: String) {
        launchWithNetworkCheck(networkState = networkState) {
            loginUseCase.signInAuthWithGoogle(idToken).onSuccess {
                updateUIState(LoginUIState(successSignIn = true))
            }
        }
    }

    fun signInAnonymously() {
        launchWithNetworkCheck(networkState = networkState) {
            loginUseCase.signInAnonymously().onSuccess {
                println("appTAG LoginViewModel signInAnonymously success")
                updateUIState(LoginUIState(successSignIn = true))
            }
        }
    }

    fun googleSignOut() {
        loginUseCase.googleSignOut()
    }

    fun googleSignIn() {
        loginUseCase.googleSignIn()
    }

    fun setLoggedInUser() {
        launchWithErrorHandling {
            preferencesRepository.setLoggedInUser(true)
        }
    }

    private fun updateUIState(newUIState: LoginUIState) {
        _uiState.value = newUIState
    }
}