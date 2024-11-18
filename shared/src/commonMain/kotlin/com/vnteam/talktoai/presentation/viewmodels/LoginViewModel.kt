package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    //val googleSignInClient: GoogleSignInClient,
) : BaseViewModel() {

    val accountExistLiveData = MutableStateFlow<Boolean?>(null)
    val isEmailAccountExistLiveData = MutableStateFlow<Boolean?>(null)
    val isGoogleAccountExistLiveData = MutableStateFlow(String.EMPTY)
    val successPasswordResetLiveData = MutableStateFlow<Boolean?>(null)
    val successSignInLiveData = MutableStateFlow<Boolean?>(null)

    fun sendPasswordResetEmail(email: String) {
        if (true/*application.isNetworkAvailable()*/) {
            showProgress()
            loginUseCase.sendPasswordResetEmail(email) { authResult ->
                when (authResult) {
                    is NetworkResult.Success -> successPasswordResetLiveData.value = true
                    is NetworkResult.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.value = 
                            it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = Constants.APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        if (true/*application.isNetworkAvailable()*/) {
            showProgress()
            loginUseCase.fetchSignInMethodsForEmail(email) { authResult ->
                when (authResult) {
                    is NetworkResult.Success -> when {
                        authResult.data.isNullOrEmpty() -> accountExistLiveData.value = true
                        idToken.isNullOrEmpty() -> isEmailAccountExistLiveData.value = true
                        else -> idToken.let { isGoogleAccountExistLiveData.value = it }
                    }
                    is NetworkResult.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.value = 
                            it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = Constants.APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        if (true/*application.isNetworkAvailable()*/) {
            showProgress()
            loginUseCase.signInWithEmailAndPassword(email, password) { authResult ->
                when (authResult) {
                    is NetworkResult.Success -> successSignInLiveData.value = true
                    is NetworkResult.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.value = 
                            it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = Constants.APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun signInAuthWithGoogle(idToken: String) {
        if (true/*application.isNetworkAvailable()*/) {
            showProgress()
            loginUseCase.signInAuthWithGoogle(idToken) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> successSignInLiveData.value = true
                    is NetworkResult.Failure -> operationResult.errorMessage?.let {
                        exceptionLiveData.value = 
                            it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = Constants.APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun signInAnonymously() {
        if (true/*application.isNetworkAvailable()*/) {
            showProgress()
            loginUseCase.signInAnonymously { authResult ->
                when (authResult) {
                    is NetworkResult.Success -> successSignInLiveData.value = true
                    is NetworkResult.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.value = 
                            it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = Constants.APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }
}