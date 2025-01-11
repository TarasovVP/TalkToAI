package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.enums.AuthState
import com.vnteam.talktoai.domain.usecase.SettingsAccountUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsAccountViewModel(
    private val settingsAccountUseCase: SettingsAccountUseCase,
    private val networkState: NetworkState
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
        successLiveData.value = true
        launchWithNetworkCheck(networkState) {
            settingsAccountUseCase.signOut().onSuccess {
                successLiveData.value = true
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        launchWithNetworkCheck(networkState) {
            settingsAccountUseCase.changePassword(currentPassword, newPassword).onSuccess {
                successChangePasswordLiveData.value = true
            }
        }
    }

    fun reAuthenticate(/*authCredential: AuthCredential*/) {
        launchWithNetworkCheck(networkState) {
            settingsAccountUseCase.reAuthenticate().onSuccess {
                reAuthenticateLiveData.value = true
            }
        }
    }

    fun deleteUser() {
        launchWithNetworkCheck(networkState) {
            settingsAccountUseCase.deleteUser().onSuccess {
                successLiveData.value = true
            }
        }
    }

    fun clearDataByKeys(keys: List<String>) {
        launchWithErrorHandling {
            settingsAccountUseCase.clearDataByKeys(keys)
        }
    }

    fun clearDataBase() {
        launchWithErrorHandling {
            settingsAccountUseCase.clearDataInDB()
        }
    }

    fun googleSignIn() {
        settingsAccountUseCase.googleSignIn()
    }
}