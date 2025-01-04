package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants.APP_NETWORK_UNAVAILABLE_REPEAT
import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.usecase.SettingsSignUpUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class SettingsSignUpViewModel(
    private val settingsSignUpUseCase: SettingsSignUpUseCase,
    private val networkState: NetworkState,
    //val googleSignInClient: GoogleSignInClient,
) : BaseViewModel() {

    val accountExistLiveData = MutableStateFlow(String.EMPTY)
    val createEmailAccountLiveData = MutableStateFlow(false)
    val createGoogleAccountLiveData = MutableStateFlow(String.EMPTY)
    val successAuthorisationLiveData = MutableStateFlow<Boolean?>(null)
    val remoteUserLiveData = MutableStateFlow<Pair<Boolean, RemoteUser?>?>(null)
    val successRemoteUserLiveData = MutableStateFlow(false)

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        if (networkState.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.fetchSignInMethodsForEmail(email) { authResult ->
                when (authResult) {
                    is NetworkResult.Success -> when {
                        authResult.data.isNullOrEmpty().not() -> {
                            accountExistLiveData.value = idToken.orEmpty()
                        }

                        idToken.isNullOrEmpty() -> createEmailAccountLiveData.value = true
                        else -> idToken.let { createGoogleAccountLiveData.value = it }
                    }

                    is NetworkResult.Failure -> authResult.errorMessage?.let {
                        _exceptionMessage.value =
                            it
                    }
                }
                hideProgress()
            }
        } else {
            _exceptionMessage.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun createUserWithGoogle(idToken: String, isExistUser: Boolean) {
        if (networkState.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.createUserWithGoogle(idToken) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> successAuthorisationLiveData.value = isExistUser
                    is NetworkResult.Failure -> operationResult.errorMessage?.let {
                        _exceptionMessage.value =
                            it
                    }
                }
                hideProgress()
            }
        } else {
            _exceptionMessage.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        if (networkState.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.createUserWithEmailAndPassword(
                email,
                password
            ) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> successAuthorisationLiveData.value = false
                    is NetworkResult.Failure -> operationResult.errorMessage?.let {
                        _exceptionMessage.value =
                            it
                    }
                }
                hideProgress()
            }
        } else {
            _exceptionMessage.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        if (networkState.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.signInWithEmailAndPassword(email, password) { authResult ->
                when (authResult) {
                    is NetworkResult.Success -> successAuthorisationLiveData.value = true
                    is NetworkResult.Failure -> authResult.errorMessage?.let {
                        _exceptionMessage.value =
                            it
                    }
                }
                hideProgress()
            }
        } else {
            _exceptionMessage.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun createRemoteUser(isExistUser: Boolean) {
        showProgress()
        launch {
            val chats = settingsSignUpUseCase.getChats().first()
            val messages = settingsSignUpUseCase.getMessages().first()
            val remoteUser = RemoteUser().apply {
                this.chats.addAll(chats)
                this.messages.addAll(messages)
            }
            remoteUserLiveData.value = Pair(isExistUser, remoteUser)
            hideProgress()
        }
    }

    fun insertRemoteCurrentUser(remoteUser: RemoteUser) {
        if (networkState.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.insertRemoteCurrentUser(remoteUser) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> successRemoteUserLiveData.value = true
                    is NetworkResult.Failure -> operationResult.errorMessage?.let {
                        _exceptionMessage.value =
                            it
                    }
                }
                hideProgress()
            }
        } else {
            _exceptionMessage.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun updateRemoteCurrentUser(remoteUser: RemoteUser) {
        if (networkState.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.updateRemoteCurrentUser(remoteUser) { operationResult ->
                when (operationResult) {
                    is NetworkResult.Success -> successRemoteUserLiveData.value = true
                    is NetworkResult.Failure -> operationResult.errorMessage?.let {
                        _exceptionMessage.value =
                            it
                    }
                }
                hideProgress()
            }
        } else {
            _exceptionMessage.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun googleSign() {
        settingsSignUpUseCase.googleSign()
    }
}