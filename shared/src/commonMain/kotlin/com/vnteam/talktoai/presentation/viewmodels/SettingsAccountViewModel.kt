package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.Constants.APP_NETWORK_UNAVAILABLE_REPEAT
import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.enums.AuthState
import com.vnteam.talktoai.domain.usecase.SettingsAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsAccountViewModel(
    private val settingsAccountUseCase: SettingsAccountUseCase,
    //val googleSignInClient: GoogleSignInClient
) : BaseViewModel() {

    val reAuthenticateLiveData = MutableStateFlow(false)
    val successLiveData = MutableStateFlow(false)
    val successChangePasswordLiveData = MutableStateFlow(false)

    fun getAuthState(): AuthState {
        return when {
            settingsAccountUseCase.isGoogleAuthUser() -> AuthState.AUTHORISED_GOOGLE
            settingsAccountUseCase.isAuthorisedUser() -> AuthState.AUTHORISED_EMAIL
            else -> AuthState.AUTHORISED_ANONYMOUSLY
        }
    }

    fun currentUserEmail(): String {
        return settingsAccountUseCase.currentUserEmail()
    }

    fun signOut() {
        if (true /*application.isNetworkAvailable()*/) {
            showProgress()
            settingsAccountUseCase.signOut { result ->
                when (result) {
                    is NetworkResult.Success -> successLiveData.value = true
                    is NetworkResult.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
            }
            hideProgress()
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        if (true /*application.isNetworkAvailable()*/) {
            showProgress()
            settingsAccountUseCase.changePassword(currentPassword, newPassword) { result ->
                when (result) {
                    is NetworkResult.Success -> successChangePasswordLiveData.value = true
                    is NetworkResult.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun reAuthenticate(/*authCredential: AuthCredential*/) {
        if (true /*application.isNetworkAvailable()*/) {
            showProgress()
            settingsAccountUseCase.reAuthenticate(/*authCredential*/) { result ->
                when (result) {
                    is NetworkResult.Success -> reAuthenticateLiveData.value = true
                    is NetworkResult.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun deleteUser() {
        if (true /*application.isNetworkAvailable()*/) {
            showProgress()
            settingsAccountUseCase.deleteUser { result ->
                when (result) {
                    is NetworkResult.Success -> successLiveData.value = true
                    is NetworkResult.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun clearDataByKeys(keys: List<String>) {
        launch {
            settingsAccountUseCase.clearDataByKeys(keys)
        }
    }

    fun clearDataBase() {
        launch {
            settingsAccountUseCase.clearDataInDB()
        }
    }
}