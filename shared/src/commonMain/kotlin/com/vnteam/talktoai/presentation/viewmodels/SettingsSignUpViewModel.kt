package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.usecase.SettingsSignUpUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow

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
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.fetchSignInMethodsForEmail(email).onSuccess { result ->
                when {
                    result.isNullOrEmpty().not() -> accountExistLiveData.value = idToken.orEmpty()
                    idToken.isNullOrEmpty() -> createEmailAccountLiveData.value = true
                    else -> idToken.let { createGoogleAccountLiveData.value = it }
                }
            }
        }
    }

    fun createUserWithGoogle(idToken: String, isExistUser: Boolean) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.createUserWithGoogle(idToken).onSuccess {
                successAuthorisationLiveData.value = isExistUser
            }
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.createUserWithEmailAndPassword(email, password).onSuccess {
                successAuthorisationLiveData.value = false
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.signInWithEmailAndPassword(email, password).onSuccess {
                successAuthorisationLiveData.value = true
            }

        }
    }

    fun createRemoteUser(isExistUser: Boolean) {
        showProgress()
        /*launchWithConditions {
            val chats = settingsSignUpUseCase.getChats().first()
            val messages = settingsSignUpUseCase.getMessages().first()
            val remoteUser = RemoteUser().apply {
                this.chats.addAll(chats)
                this.messages.addAll(messages)
            }
            remoteUserLiveData.value = Pair(isExistUser, remoteUser)
            hideProgress()
        }*/
    }

    fun insertRemoteCurrentUser(remoteUser: RemoteUser) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.insertRemoteCurrentUser(remoteUser).onSuccess {
                successRemoteUserLiveData.value = true
            }

        }
    }

    fun updateRemoteCurrentUser(remoteUser: RemoteUser) {
        launchWithNetworkCheck(networkState) {
            settingsSignUpUseCase.updateRemoteCurrentUser(remoteUser).onSuccess {
                successRemoteUserLiveData.value = true
            }
        }
    }

    fun googleSign() {
        settingsSignUpUseCase.googleSign()
    }
}