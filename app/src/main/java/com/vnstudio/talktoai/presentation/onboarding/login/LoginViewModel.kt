package com.vnstudio.talktoai.presentation.onboarding.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.data.network.Result
import com.vnstudio.talktoai.domain.usecases.LoginUseCase
import com.vnstudio.talktoai.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val loginUseCase: LoginUseCase
) : BaseViewModel(application) {

    val accountExistLiveData = MutableLiveData<Unit>()
    val isEmailAccountExistLiveData = MutableLiveData<Unit>()
    val isGoogleAccountExistLiveData = MutableLiveData<String>()
    val successPasswordResetLiveData = MutableLiveData<Boolean>()
    val successSignInLiveData = MutableLiveData<Unit>()

    fun sendPasswordResetEmail(email: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.sendPasswordResetEmail(email) { authResult ->
                when(authResult) {
                    is Result.Success -> successPasswordResetLiveData.postValue(true)
                    is Result.Failure -> authResult.errorMessage?.let { exceptionLiveData.postValue(it) }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue("Network is unavailable")
        }
    }

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.fetchSignInMethodsForEmail(email) { authResult ->
                when(authResult) {
                    is Result.Success -> when {
                        authResult.data.isNullOrEmpty() -> accountExistLiveData.postValue(Unit)
                        idToken.isNullOrEmpty() -> isEmailAccountExistLiveData.postValue(Unit)
                        else -> idToken.let { isGoogleAccountExistLiveData.postValue(it) }
                    }
                    is Result.Failure -> authResult.errorMessage?.let { isGoogleAccountExistLiveData.postValue(it) }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue("Network is unavailable")
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.signInWithEmailAndPassword(email, password) { authResult ->
                when(authResult) {
                    is Result.Success -> successSignInLiveData.postValue(Unit)
                    is Result.Failure -> authResult.errorMessage?.let { exceptionLiveData.postValue(it) }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue("Network is unavailable")
        }
    }

    fun signInAuthWithGoogle(idToken: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.signInAuthWithGoogle(idToken) { operationResult ->
                when(operationResult) {
                    is Result.Success -> successSignInLiveData.postValue(Unit)
                    is Result.Failure -> operationResult.errorMessage?.let { exceptionLiveData.postValue(it) }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue("Network is unavailable")
        }
    }

    fun signInAnonymously() {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.signInAnonymously { authResult ->
                when(authResult) {
                    is Result.Success -> successSignInLiveData.postValue(Unit)
                    is Result.Failure -> authResult.errorMessage?.let { exceptionLiveData.postValue(it) }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue("Network is unavailable")
        }
    }
}