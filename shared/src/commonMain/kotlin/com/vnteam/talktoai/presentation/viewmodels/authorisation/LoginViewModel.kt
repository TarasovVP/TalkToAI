package com.vnteam.talktoai.presentation.viewmodels.authorisation

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.usecase.execute
import com.vnteam.talktoai.presentation.uistates.LoginUIState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleSignInUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ResetPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInAnonymouslyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.IdTokenUseCase
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
    private val idTokenUseCase: IdTokenUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        launchWithErrorHandling {
            when (val result = signInWithEmailAndPasswordUseCase.execute(Pair(email, password))) {
                is com.vnteam.talktoai.data.network.Result.Success -> {
                    hideProgress()
                    idTokenUseCase.set(result.data?.idToken.orEmpty())
                    userEmailUseCase.set(result.data?.email.orEmpty())
                    updateUIState(LoginUIState(emailSignInSuccess = true))
                }
                is com.vnteam.talktoai.data.network.Result.Failure -> onError(Exception(result.errorMessage))
                is com.vnteam.talktoai.data.network.Result.Loading -> showProgress()
            }
        }
    }

    fun signInAnonymously() {
        launchWithErrorHandling {
            when (val result = signInAnonymouslyUseCase.execute()) {
                is com.vnteam.talktoai.data.network.Result.Success -> {
                    hideProgress()
                    idTokenUseCase.set(result.data?.idToken.orEmpty())
                    updateUIState(LoginUIState(anonymousSignInSuccess = true))
                }
                is com.vnteam.talktoai.data.network.Result.Failure -> onError(Exception(result.errorMessage))
                is com.vnteam.talktoai.data.network.Result.Loading -> showProgress()
            }
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
        launchWithErrorHandling {
            when (val result = googleUseCase.execute("")) {
                is com.vnteam.talktoai.data.network.Result.Success -> {
                    val token = result.data.orEmpty()
                    idTokenUseCase.set(token)
                    updateUIState(LoginUIState(googleSignInSuccess = true))
                }
                is com.vnteam.talktoai.data.network.Result.Failure -> onError(Exception(result.errorMessage))
                is com.vnteam.talktoai.data.network.Result.Loading -> showProgress()
            }
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