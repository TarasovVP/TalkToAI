package com.vnstudio.talktoai.presentation.screens.settings.settings_account

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.AppDatabase
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsAccountUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsAccountViewModel @Inject constructor(
    private val application: Application,
    private val settingsAccountUseCase: SettingsAccountUseCase,
) : BaseViewModel(application) {

    val reAuthenticateLiveData = MutableLiveData<Unit>()
    val successLiveData = MutableLiveData<Unit>()
    val successChangePasswordLiveData = MutableLiveData<Unit>()

    fun isAuthorisedUser(): Boolean {
        return settingsAccountUseCase.isAuthorisedUser()
    }

    fun isGoogleAuthUser(): Boolean {
        return settingsAccountUseCase.isGoogleAuthUser()
    }

    fun currentUserEmail(): String {
        return settingsAccountUseCase.currentUserEmail()
    }

    fun signOut() {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsAccountUseCase.signOut { result ->
                when (result) {
                    is Result.Success -> successLiveData.postValue(Unit)
                    is Result.Failure -> exceptionLiveData.postValue(result.errorMessage.orEmpty())
                }
            }
            hideProgress()
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsAccountUseCase.changePassword(currentPassword, newPassword) { result ->
                when (result) {
                    is Result.Success -> successChangePasswordLiveData.postValue(Unit)
                    is Result.Failure -> exceptionLiveData.postValue(result.errorMessage.orEmpty())
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
        }
    }

    fun reAuthenticate(authCredential: AuthCredential) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsAccountUseCase.reAuthenticate(authCredential) { result ->
                when (result) {
                    is Result.Success -> reAuthenticateLiveData.postValue(Unit)
                    is Result.Failure -> exceptionLiveData.postValue(result.errorMessage.orEmpty())
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
        }
    }

    fun deleteUser() {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsAccountUseCase.deleteUser { result ->
                when (result) {
                    is Result.Success -> successLiveData.postValue(Unit)
                    is Result.Failure -> exceptionLiveData.postValue(result.errorMessage.orEmpty())
                }
                hideProgress()
            }
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
        }
    }

    fun clearDataByKeys(keys: List<Preferences.Key<*>>) {
        launch {
            settingsAccountUseCase.clearDataByKeys(keys)
        }
    }

    fun clearDataBase() {
        AppDatabase.getDatabase(application).clearAllTables()
    }
}