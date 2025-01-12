package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.usecase.SettingsAccountUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ChangePasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ReAuthenticateUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserLoginUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsAccountViewModel(
    private val settingsAccountUseCase: SettingsAccountUseCase,
    private val networkState: NetworkState,


    private val userLoginUseCase: UserLoginUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val reAuthenticateUseCase: ReAuthenticateUseCase,
    private val googleUseCase: GoogleUseCase
) : BaseViewModel() {

    private val _userLogin = MutableStateFlow<String?>(null)
    val userLogin = _userLogin.asStateFlow()

    val reAuthenticateLiveData = MutableStateFlow(false)
    val successLiveData = MutableStateFlow(false)
    val successChangePasswordLiveData = MutableStateFlow(false)

    fun currentUserEmail() {
        launchWithResultHandling {
            userLoginUseCase.getUserLogin().onSuccess {
                _userLogin.value = it
            }
        }
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
            changePasswordUseCase.execute(currentPassword, newPassword).onSuccess {
                successChangePasswordLiveData.value = true
            }
        }
    }

    fun reAuthenticate(/*authCredential: AuthCredential*/) {
        launchWithNetworkCheck(networkState) {
            reAuthenticateUseCase.execute().onSuccess {
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
        googleUseCase.googleSignIn()
    }
}