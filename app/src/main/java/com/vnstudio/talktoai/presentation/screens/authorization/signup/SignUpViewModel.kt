package com.vnstudio.talktoai.presentation.screens.authorization.signup

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SignUpUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val application: Application,
    private val signUpUseCase: SignUpUseCase,
    val googleSignInClient: GoogleSignInClient,
) : BaseViewModel(application) {

    val accountExistLiveData = MutableLiveData<Unit>()
    val createEmailAccountLiveData = MutableLiveData<Unit>()
    val createGoogleAccountLiveData = MutableLiveData<String>()
    val successSignUpLiveData = MutableLiveData<Unit>()
    val createCurrentUserLiveData = MutableLiveData<Unit>()

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        if (application.isNetworkAvailable()) {
            showProgress()
            signUpUseCase.fetchSignInMethodsForEmail(email) { authResult ->
                when (authResult) {
                    is Result.Success -> when {
                        authResult.data.isNullOrEmpty()
                            .not() -> accountExistLiveData.postValue(Unit)
                        idToken.isNullOrEmpty() -> createEmailAccountLiveData.postValue(Unit)
                        else -> idToken.let { createGoogleAccountLiveData.postValue(it) }
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

    fun createUserWithGoogle(idToken: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            signUpUseCase.createUserWithGoogle(idToken) { operationResult ->
                when (operationResult) {
                    is Result.Success -> successSignUpLiveData.postValue(Unit)
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

    fun createUserWithEmailAndPassword(email: String, password: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            signUpUseCase.createUserWithEmailAndPassword(email, password) { operationResult ->
                when (operationResult) {
                    is Result.Success -> successSignUpLiveData.postValue(Unit)
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

    fun insertRemoteUser(remoteUser: RemoteUser) {
        if (application.isNetworkAvailable()) {
            showProgress()
            signUpUseCase.insertRemoteUser(remoteUser) { operationResult ->
                when (operationResult) {
                    is Result.Success -> createCurrentUserLiveData.postValue(Unit)
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
}