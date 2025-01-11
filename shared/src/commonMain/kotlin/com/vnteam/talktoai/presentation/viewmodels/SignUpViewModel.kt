package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.usecase.SignUpUseCase
import com.vnteam.talktoai.presentation.SignUpUIState
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val networkState: NetworkState
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState: StateFlow<SignUpUIState> = _uiState.asStateFlow()

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        launchWithConditions(networkState) {
            showProgress()
            /*signUpUseCase.fetchSignInMethodsForEmail(email) { authResult ->
                when (authResult) {
                    is NetworkResult.Success -> when {
                        authResult.data.isNullOrEmpty()
                            .not() -> updateUIState(SignUpUIState(accountExist = true))

                        idToken.isNullOrEmpty() -> updateUIState(SignUpUIState(createEmailAccount = true))
                        else -> updateUIState(SignUpUIState(createGoogleAccount = idToken))
                    }

                    is NetworkResult.Failure -> authResult.errorMessage?.let {
                        _exceptionMessage.value = it
                    }
                }
                hideProgress()
            }*/
        }
    }

    fun createUserWithGoogle(idToken: String) {
        /*launchWithConditions(networkState) {
            showProgress()
            signUpUseCase.createUserWithGoogle(idToken) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> updateUIState(SignUpUIState(successSignUp = true))
                    is NetworkResult.Failure -> operationResult.errorMessage?.let {
                        _exceptionMessage.value =
                            it
                    }
                }
                hideProgress()
            }
        }*/
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        /*launchWithConditions(networkState) {
            showProgress()
            signUpUseCase.createUserWithEmailAndPassword(email, password) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> updateUIState(SignUpUIState(successSignUp = true))
                    is NetworkResult.Failure -> operationResult.errorMessage?.let {
                        _exceptionMessage.value =
                            it
                    }
                }
                hideProgress()
            }
        }*/
    }

    fun insertRemoteUser(remoteUser: RemoteUser) {
        /*launchWithConditions(networkState) {
            showProgress()
            signUpUseCase.insertRemoteUser(remoteUser) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> updateUIState(SignUpUIState(createCurrentUser = true))
                    is NetworkResult.Failure -> operationResult.errorMessage?.let {
                        _exceptionMessage.value =
                            it
                    }
                }
                hideProgress()
            }
        }*/
    }

    private fun updateUIState(newUIState: SignUpUIState) {
        _uiState.value = newUIState
    }

    fun googleSignOut() {
        signUpUseCase.googleSignOut()
    }

    fun googleSignIn() {
        signUpUseCase.googleSignIn()
    }
}