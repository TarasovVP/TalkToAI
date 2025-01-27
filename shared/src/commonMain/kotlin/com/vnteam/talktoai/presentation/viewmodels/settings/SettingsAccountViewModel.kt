package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.usecase.execute
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ChangePasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ClearDataUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.DeleteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ReAuthenticateUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithGoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignOutUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsAccountViewModel(
    private val networkState: NetworkState,
    private val userEmailUseCase: UserEmailUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val reAuthenticateUseCase: ReAuthenticateUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val googleUseCase: SignInWithGoogleUseCase
) : BaseViewModel() {

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail = _userEmail.asStateFlow()

    val reAuthenticateLiveData = MutableStateFlow(false)
    val successLiveData = MutableStateFlow(false)
    val successChangePasswordLiveData = MutableStateFlow(false)

    fun currentUserEmail() {
        launchWithResultHandling {
            userEmailUseCase.get().onSuccess {
                _userEmail.value = it
            }
        }
    }

    fun signOut() {
        successLiveData.value = true
        launchWithResult {
            signOutUseCase.execute().onSuccess {
                successLiveData.value = true
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        launchWithResult {
            changePasswordUseCase.execute(Pair(currentPassword, newPassword)).onSuccess {
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
        launchWithResult {
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
        val someString = "someString"
        launchWithResult {
            googleUseCase.execute(someString)
        }
    }
}