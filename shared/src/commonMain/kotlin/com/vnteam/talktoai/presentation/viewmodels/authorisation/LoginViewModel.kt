package com.vnteam.talktoai.presentation.viewmodels.authorisation

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.usecase.execute
import com.vnteam.talktoai.presentation.uistates.LoginUIState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleSignInUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ResetPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInAnonymouslyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val userEmailUseCase: UserEmailUseCase,
    private val googleUseCase: GoogleSignInUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        launchWithResult {
            signInWithEmailAndPasswordUseCase.execute(Pair(email, password))
        }
    }

    fun signInAnonymously() {
        launchWithResult {
            signInAnonymouslyUseCase.execute()
        }
    }

    fun resetPassword(email: String) {
        launchWithResult {
            resetPasswordUseCase.execute(email).onSuccess {
                updateUIState(LoginUIState(successPasswordReset = true))
            }
        }
    }

    fun googleSignIn() {
        val someString = "someString"
        launchWithResult {
            googleUseCase.execute(someString)
        }
    }


    /*fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
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

    fun signInAuthWithGoogle(idToken: String) {
        launchWithNetworkCheck(networkState = networkState) {
            signInWithGoogleUseCase.execute(idToken).onSuccess { userLogin ->
                setUserLogin(userLogin.orEmpty())
            }
        }
    }



    fun googleSignOut() {
        googleUseCase.googleSignOut()
    }

    fun googleSignIn() {
        googleUseCase.googleSignIn()
    }*/

    private fun setUserLogin(userLogin: String) {
        launchWithErrorHandling {
            userEmailUseCase.set(userLogin)
        }
    }

    private fun updateUIState(newUIState: LoginUIState) {
        _uiState.value = newUIState
    }
}