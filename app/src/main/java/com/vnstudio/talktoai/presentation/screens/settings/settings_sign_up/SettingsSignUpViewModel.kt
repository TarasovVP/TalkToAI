package com.vnstudio.talktoai.presentation.screens.settings.settings_sign_up

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsSignUpUseCase
import com.vnstudio.talktoai.infrastructure.Constants.APP_NETWORK_UNAVAILABLE_REPEAT
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class SettingsSignUpViewModel(
    private val application: Application,
    private val settingsSignUpUseCase: SettingsSignUpUseCase,
    val googleSignInClient: GoogleSignInClient,
) : BaseViewModel(application) {

    val accountExistLiveData = MutableStateFlow(String.EMPTY)
    val createEmailAccountLiveData = MutableStateFlow(false)
    val createGoogleAccountLiveData = MutableStateFlow(String.EMPTY)
    val successAuthorisationLiveData = MutableStateFlow<Boolean?>(null)
    val remoteUserLiveData = MutableStateFlow<Pair<Boolean, RemoteUser?>?>(null)
    val successRemoteUserLiveData = MutableStateFlow(false)

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.fetchSignInMethodsForEmail(email) { authResult ->
                when (authResult) {
                    is Result.Success -> when {
                        authResult.data.isNullOrEmpty().not() -> {
                            accountExistLiveData.value = idToken.orEmpty()
                        }
                        idToken.isNullOrEmpty() -> createEmailAccountLiveData.value = true
                        else -> idToken.let { createGoogleAccountLiveData.value = it }
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

    fun createUserWithGoogle(idToken: String, isExistUser: Boolean) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.createUserWithGoogle(idToken) { operationResult ->
                when (operationResult) {
                    is Result.Success -> successAuthorisationLiveData.value = isExistUser
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

    fun createUserWithEmailAndPassword(email: String, password: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.createUserWithEmailAndPassword(
                email,
                password
            ) { operationResult ->
                when (operationResult) {
                    is Result.Success -> successAuthorisationLiveData.value = false
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

    fun signInWithEmailAndPassword(email: String, password: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.signInWithEmailAndPassword(email, password) { authResult ->
                when (authResult) {
                    is Result.Success -> successAuthorisationLiveData.value = true
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
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.insertRemoteCurrentUser(remoteUser) { operationResult ->
                when (operationResult) {
                    is Result.Success -> successRemoteUserLiveData.value = true
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

    fun updateRemoteCurrentUser(remoteUser: RemoteUser) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsSignUpUseCase.updateRemoteCurrentUser(remoteUser) { operationResult ->
                when (operationResult) {
                    is Result.Success -> successRemoteUserLiveData.value = true
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
}