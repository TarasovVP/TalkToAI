package com.vnstudio.talktoai.presentation.screens.authorization.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.LoginUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val loginUseCase: LoginUseCase,
    val googleSignInClient: GoogleSignInClient,
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
                when (authResult) {
                    is Result.Success -> successPasswordResetLiveData.postValue(true)
                    is Result.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.postValue(
                            it
                        )
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
        }
    }

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.fetchSignInMethodsForEmail(email) { authResult ->
                when (authResult) {
                    is Result.Success -> when {
                        authResult.data.isNullOrEmpty() -> accountExistLiveData.postValue(Unit)
                        idToken.isNullOrEmpty() -> isEmailAccountExistLiveData.postValue(Unit)
                        else -> idToken.let { isGoogleAccountExistLiveData.postValue(it) }
                    }
                    is Result.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.postValue(
                            it
                        )
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.signInWithEmailAndPassword(email, password) { authResult ->
                when (authResult) {
                    is Result.Success -> successSignInLiveData.postValue(Unit)
                    is Result.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.postValue(
                            it
                        )
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
        }
    }

    fun signInAuthWithGoogle(idToken: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.signInAuthWithGoogle(idToken) { operationResult ->
                when (operationResult) {
                    is Result.Success -> successSignInLiveData.postValue(Unit)
                    is Result.Failure -> operationResult.errorMessage?.let {
                        exceptionLiveData.postValue(
                            it
                        )
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
        }
    }

    fun signInAnonymously() {
        if (application.isNetworkAvailable()) {
            showProgress()
            loginUseCase.signInAnonymously { authResult ->
                when (authResult) {
                    is Result.Success -> successSignInLiveData.postValue(Unit)
                    is Result.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.postValue(
                            it
                        )
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
        }
    }
}