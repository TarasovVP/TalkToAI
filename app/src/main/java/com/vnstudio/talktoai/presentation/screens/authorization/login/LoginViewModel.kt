package com.vnstudio.talktoai.presentation.screens.authorization.login

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.LoginUseCase
import com.vnstudio.talktoai.infrastructure.Constants.APP_NETWORK_UNAVAILABLE_REPEAT
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel(
    private val application: Application,
    private val loginUseCase: LoginUseCase,
    val googleSignInClient: GoogleSignInClient,
) : BaseViewModel(application) {

    val accountExistLiveData = MutableStateFlow(Unit)
    val isEmailAccountExistLiveData = MutableStateFlow(Unit)
    val isGoogleAccountExistLiveData = MutableStateFlow(String.EMPTY)
    val successPasswordResetLiveData = MutableStateFlow(false)
    val successSignInLiveData = MutableStateFlow(Unit)

    fun sendPasswordResetEmail(email: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.sendPasswordResetEmail(email) { authResult ->
                when (authResult) {
                    is Result.Success -> successPasswordResetLiveData.value = true
                    is Result.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.value = 
                            it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.fetchSignInMethodsForEmail(email) { authResult ->
                when (authResult) {
                    is Result.Success -> when {
                        authResult.data.isNullOrEmpty() -> accountExistLiveData.value = Unit
                        idToken.isNullOrEmpty() -> isEmailAccountExistLiveData.value = Unit
                        else -> idToken.let { isGoogleAccountExistLiveData.value = it }
                    }
                    is Result.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.value = 
                            it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.signInWithEmailAndPassword(email, password) { authResult ->
                when (authResult) {
                    is Result.Success -> successSignInLiveData.value = Unit
                    is Result.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.value = 
                            it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun signInAuthWithGoogle(idToken: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.signInAuthWithGoogle(idToken) { operationResult ->
                when (operationResult) {
                    is Result.Success -> successSignInLiveData.value = Unit
                    is Result.Failure -> operationResult.errorMessage?.let {
                        exceptionLiveData.value = 
                            it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun signInAnonymously() {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.signInAnonymously { authResult ->
                when (authResult) {
                    is Result.Success -> successSignInLiveData.value = Unit
                    is Result.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.value = 
                            it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }
}