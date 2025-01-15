package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.presentation.uistates.LoginUIState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.FetchSignInMethodsForEmailUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ResetPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInAnonymouslyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithGoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.preferences.UserLoginUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val networkState: NetworkState,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val fetchSignInMethodsForEmailUseCase: FetchSignInMethodsForEmailUseCase,
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val userLoginUseCase: UserLoginUseCase,
    private val googleUseCase: GoogleUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun resetPassword(email: String) {
        launchWithNetworkCheck(networkState = networkState) {
            resetPasswordUseCase.execute(email).onSuccess {
                updateUIState(LoginUIState(successPasswordReset = true))
            }
        }
    }

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        launchWithNetworkCheck(networkState = networkState) {
            fetchSignInMethodsForEmailUseCase.execute(email).onSuccess { data ->
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
            signInWithEmailAndPasswordUseCase.execute(email, password).onSuccess { userLogin ->
                setUserLogin(userLogin.orEmpty())
            }
        }
    }

    fun signInAuthWithGoogle(idToken: String) {
        launchWithNetworkCheck(networkState = networkState) {
            signInWithGoogleUseCase.execute(idToken).onSuccess { userLogin ->
                setUserLogin(userLogin.orEmpty())
            }
        }
    }

    fun signInAnonymously() {
        launchWithNetworkCheck(networkState = networkState) {
            signInAnonymouslyUseCase.execute().onSuccess { userLogin ->
                println("appTAG LoginViewModel signInAnonymously success userLogin $userLogin")
                setUserLogin(userLogin.orEmpty())
            }
        }
    }

    fun googleSignOut() {
        googleUseCase.googleSignOut()
    }

    fun googleSignIn() {
        googleUseCase.googleSignIn()
    }

    private fun setUserLogin(userLogin: String) {
        launchWithErrorHandling {
            userLoginUseCase.setUserLogin(userLogin)
        }
    }

    private fun updateUIState(newUIState: LoginUIState) {
        _uiState.value = newUIState
    }
}