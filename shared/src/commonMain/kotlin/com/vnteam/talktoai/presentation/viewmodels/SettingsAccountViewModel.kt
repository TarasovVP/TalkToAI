package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ChangePasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ClearDataUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.DeleteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ReAuthenticateUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignOutUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserLoginUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsAccountViewModel(
    private val networkState: NetworkState,
    private val userLoginUseCase: UserLoginUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val reAuthenticateUseCase: ReAuthenticateUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val clearDataUseCase: ClearDataUseCase,
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
            signOutUseCase.execute().onSuccess {
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
            deleteUserUseCase.execute().onSuccess {
                successLiveData.value = true
            }
        }
    }

    fun clearData() {
        launchWithErrorHandling {
            clearDataUseCase.execute()
        }
    }

    fun googleSignIn() {
        googleUseCase.googleSignIn()
    }
}