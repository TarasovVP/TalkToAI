package com.vnstudio.talktoai.presentation.screens.authorization.signup

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SignUpUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



class SignUpViewModel(
    private val application: Application,
    private val signUpUseCase: SignUpUseCase,
    val googleSignInClient: GoogleSignInClient,
) : BaseViewModel(application) {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState: StateFlow<SignUpUIState> = _uiState.asStateFlow()

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        if (application.isNetworkAvailable()) {
            showProgress()
            signUpUseCase.fetchSignInMethodsForEmail(email) { authResult ->
                when (authResult) {
                    is Result.Success -> when {
                        authResult.data.isNullOrEmpty()
                            .not() -> updateUIState(SignUpUIState(accountExist = true))
                        idToken.isNullOrEmpty() -> updateUIState(SignUpUIState(createEmailAccount = true))
                        else -> updateUIState(SignUpUIState(createGoogleAccount = idToken))
                    }
                    is Result.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.postValue(it)
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
                    is Result.Success -> updateUIState(SignUpUIState(successSignUp = true))
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
                    is Result.Success -> updateUIState(SignUpUIState(successSignUp = true))
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
                    is Result.Success ->updateUIState(SignUpUIState(createCurrentUser = true))
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

    private fun updateUIState(newUIState: SignUpUIState) {
        _uiState.value = newUIState
    }
}