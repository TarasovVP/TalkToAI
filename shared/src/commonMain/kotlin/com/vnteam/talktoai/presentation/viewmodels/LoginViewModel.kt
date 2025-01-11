package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.LoginUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    val accountExistLiveData = MutableStateFlow<Boolean?>(null)
    val isEmailAccountExistLiveData = MutableStateFlow<Boolean?>(null)
    val isGoogleAccountExistLiveData = MutableStateFlow(String.EMPTY)
    val successPasswordResetLiveData = MutableStateFlow<Boolean?>(null)
    val successSignInLiveData = MutableStateFlow<Boolean?>(null)

    fun sendPasswordResetEmail(email: String) {
        launchWithNetworkCheck(networkState = networkState) {
            loginUseCase.sendPasswordResetEmail(email).onSuccess {
                successPasswordResetLiveData.value = true
            }
        }
    }

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        launchWithNetworkCheck(networkState = networkState) {
            loginUseCase.fetchSignInMethodsForEmail(email).onSuccess { data ->
                when {
                    data.isNullOrEmpty() -> accountExistLiveData.value = true
                    idToken.isNullOrEmpty() -> isEmailAccountExistLiveData.value = true
                    else -> idToken.let { isGoogleAccountExistLiveData.value = it }
                }
                successSignInLiveData.value = true
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        launchWithNetworkCheck(networkState = networkState) {
            loginUseCase.signInWithEmailAndPassword(email, password).onSuccess {
                successSignInLiveData.value = true
            }
        }
    }

    fun signInAuthWithGoogle(idToken: String) {
        launchWithNetworkCheck(networkState = networkState) {
            loginUseCase.signInAuthWithGoogle(idToken).onSuccess {
                successSignInLiveData.value = true
            }
        }
    }

    fun signInAnonymously() {
        launchWithNetworkCheck(networkState = networkState) {
            loginUseCase.signInAnonymously().onSuccess {
                successSignInLiveData.value = true
            }
        }
    }

    fun googleSignOut() {
        loginUseCase.googleSignOut()
    }

    fun googleSignIn() {
        loginUseCase.googleSignIn()
    }
}