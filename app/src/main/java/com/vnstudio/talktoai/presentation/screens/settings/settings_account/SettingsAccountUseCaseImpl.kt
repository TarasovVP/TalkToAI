package com.vnstudio.talktoai.presentation.screens.settings.settings_account

import com.google.firebase.auth.AuthCredential
import com.vnstudio.talktoai.domain.repositories.AuthRepository
import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsAccountUseCase


class SettingsAccountUseCaseImpl(
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
    private val dataStoreRepository: DataStoreRepository,
) : SettingsAccountUseCase {

    override fun isLoggedInUser() = authRepository.isLoggedInUser()

    override fun isAuthorisedUser() = authRepository.isAuthorisedUser()

    override fun isGoogleAuthUser() = authRepository.isGoogleAuthUser()

    override fun currentUserEmail() = authRepository.currentUserEmail()

    override fun signOut(result: (Result<Unit>) -> Unit) = authRepository.signOut { authResult ->
        result.invoke(authResult)
    }

    override fun changePassword(
        currentPassword: String,
        newPassword: String,
        result: (Result<Unit>) -> Unit,
    ) = authRepository.changePassword(currentPassword, newPassword) { authResult ->
        result.invoke(authResult)
    }

    override fun reAuthenticate(authCredential: AuthCredential, result: (Result<Unit>) -> Unit) =
        authRepository.reAuthenticate(authCredential) { authResult ->
            result.invoke(authResult)
        }

    override fun deleteUser(result: (Result<Unit>) -> Unit) =
        realDataBaseRepository.deleteRemoteUser {
            authRepository.deleteUser(result)
        }

    override suspend fun clearDataByKeys(keys: List<String>) =
        dataStoreRepository.clearDataByKeys(keys)
}