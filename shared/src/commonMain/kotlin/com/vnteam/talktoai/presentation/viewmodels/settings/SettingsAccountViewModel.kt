package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.usecase.execute
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ChangePasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ClearDataUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.DeleteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ReAuthenticateUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.ClearLocalDataUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.IdTokenUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UidUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsAccountViewModel(
    private val networkState: NetworkState,
    private val idTokenUseCase: IdTokenUseCase,
    private val userEmailUseCase: UserEmailUseCase,
    private val uidUseCase: UidUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val reAuthenticateUseCase: ReAuthenticateUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val clearLocalDataUseCase: ClearLocalDataUseCase,
) : BaseViewModel() {

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail = _userEmail.asStateFlow()

    val reAuthenticateLiveData = MutableStateFlow(false)
    val successLiveData = MutableStateFlow(false)
    val successChangePasswordLiveData = MutableStateFlow(false)

    fun currentUserEmail() {
        launchWithResultHandling {
            userEmailUseCase.get().onSuccess {
                _userEmail.value = it.orEmpty()
            }
        }
    }

    fun signOut() {
        launchWithErrorHandling {
            idTokenUseCase.set(String.EMPTY)
            userEmailUseCase.set(String.EMPTY)
            uidUseCase.set(String.EMPTY)
            successLiveData.value = true
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        launchWithErrorHandling {
            val email = _userEmail.value.orEmpty()
            val reAuthResult = reAuthenticateUseCase.execute(Pair(email, currentPassword))
            if (reAuthResult is Result.Failure) {
                onError(Exception(reAuthResult.errorMessage))
                return@launchWithErrorHandling
            }
            val freshIdToken = (reAuthResult as Result.Success).data.orEmpty()
            idTokenUseCase.set(freshIdToken)
            val changeResult = changePasswordUseCase.execute(Pair(freshIdToken, newPassword))
            if (changeResult is Result.Success) {
                successChangePasswordLiveData.value = true
            } else if (changeResult is Result.Failure) {
                onError(Exception(changeResult.errorMessage))
            }
        }
    }

    fun deleteUser(password: String) {
        launchWithErrorHandling {
            val email = _userEmail.value.orEmpty()
            val reAuthResult = reAuthenticateUseCase.execute(Pair(email, password))
            if (reAuthResult is Result.Failure) {
                onError(Exception(reAuthResult.errorMessage))
                return@launchWithErrorHandling
            }
            val freshIdToken = (reAuthResult as Result.Success).data
            idTokenUseCase.set(freshIdToken.orEmpty())
            val deleteResult = deleteUserUseCase.execute(freshIdToken)
            if (deleteResult is Result.Success) {
                successLiveData.value = true
            } else if (deleteResult is Result.Failure) {
                onError(Exception(deleteResult.errorMessage))
            }
        }
    }

    fun clearData() {
        launchWithErrorHandling {
            clearLocalDataUseCase.execute()
            clearDataUseCase.execute()
        }
    }
}
