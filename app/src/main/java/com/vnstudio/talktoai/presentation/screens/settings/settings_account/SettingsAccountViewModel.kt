package com.vnstudio.talktoai.presentation.screens.settings.settings_account

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable

import com.vnstudio.talktoai.domain.enums.AuthState
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsAccountUseCase
import com.vnstudio.talktoai.infrastructure.Constants.APP_NETWORK_UNAVAILABLE_REPEAT
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class SettingsAccountViewModel(
    private val application: Application,
    private val settingsAccountUseCase: SettingsAccountUseCase,
    val googleSignInClient: GoogleSignInClient
) : BaseViewModel(application) {

    val reAuthenticateLiveData = MutableStateFlow(Unit)
    val successLiveData = MutableStateFlow(Unit)
    val successChangePasswordLiveData = MutableStateFlow(Unit)

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
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsAccountUseCase.signOut { result ->
                when (result) {
                    is Result.Success -> successLiveData.value = Unit
                    is Result.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
            }
            hideProgress()
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsAccountUseCase.changePassword(currentPassword, newPassword) { result ->
                when (result) {
                    is Result.Success -> successChangePasswordLiveData.value = Unit
                    is Result.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun reAuthenticate(authCredential: AuthCredential) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsAccountUseCase.reAuthenticate(authCredential) { result ->
                when (result) {
                    is Result.Success -> reAuthenticateLiveData.value = Unit
                    is Result.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun deleteUser() {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsAccountUseCase.deleteUser { result ->
                when (result) {
                    is Result.Success -> successLiveData.value = Unit
                    is Result.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    fun clearDataByKeys(keys: List<Preferences.Key<*>>) {
        launch {
            settingsAccountUseCase.clearDataByKeys(keys)
        }
    }

    fun clearDataBase() {
        //TODO implement with sqlDelight
        //AppDatabase.getDatabase(application).clearAllTables()
    }
}