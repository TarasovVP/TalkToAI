package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.usecase.SignUpUseCase
import com.vnteam.talktoai.presentation.SignUpUIState
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val networkState: NetworkState,
    //val googleSignInClient: GoogleSignInClient,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState: StateFlow<SignUpUIState> = _uiState.asStateFlow()

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        if (networkState.isNetworkAvailable()) {
            showProgress()
            signUpUseCase.fetchSignInMethodsForEmail(email) { authResult ->
                when (authResult) {
                    is NetworkResult.Success -> when {
                        authResult.data.isNullOrEmpty()
                            .not() -> updateUIState(SignUpUIState(accountExist = true))

                        idToken.isNullOrEmpty() -> updateUIState(SignUpUIState(createEmailAccount = true))
                        else -> updateUIState(SignUpUIState(createGoogleAccount = idToken))
                    }

                    is NetworkResult.Failure -> authResult.errorMessage?.let {
                        exceptionLiveData.value = it
                    }
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = Constants.APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun createUserWithGoogle(idToken: String) {
        if (networkState.isNetworkAvailable()) {
            showProgress()
            signUpUseCase.createUserWithGoogle(idToken) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> updateUIState(SignUpUIState(successSignUp = true))
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

    fun createUserWithEmailAndPassword(email: String, password: String) {
        if (networkState.isNetworkAvailable()) {
            showProgress()
            signUpUseCase.createUserWithEmailAndPassword(email, password) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> updateUIState(SignUpUIState(successSignUp = true))
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

    fun insertRemoteUser(remoteUser: RemoteUser) {
        if (networkState.isNetworkAvailable()) {
            showProgress()
            signUpUseCase.insertRemoteUser(remoteUser) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> updateUIState(SignUpUIState(createCurrentUser = true))
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

    private fun updateUIState(newUIState: SignUpUIState) {
        _uiState.value = newUIState
    }
}