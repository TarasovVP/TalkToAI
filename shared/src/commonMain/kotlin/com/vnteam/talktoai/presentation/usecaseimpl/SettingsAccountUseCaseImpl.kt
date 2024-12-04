package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.SettingsAccountUseCase

class SettingsAccountUseCaseImpl(
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
    private val preferencesRepository: PreferencesRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : SettingsAccountUseCase {

    override fun isLoggedInUser() = authRepository.isLoggedInUser()

    override fun isAuthorisedUser() = authRepository.isAuthorisedUser()

    override fun isGoogleAuthUser() = authRepository.isGoogleAuthUser()

    override fun currentUserEmail() = authRepository.currentUserEmail()

    override fun signOut(result: (NetworkResult<Unit>) -> Unit) =
        authRepository.signOut { authResult ->
            result.invoke(authResult)
        }

    override fun changePassword(
        currentPassword: String,
        newPassword: String,
        result: (NetworkResult<Unit>) -> Unit,
    ) = authRepository.changePassword(currentPassword, newPassword) { authResult ->
        result.invoke(authResult)
    }

    override fun reAuthenticate(/*authCredential: AuthCredential, */result: (NetworkResult<Unit>) -> Unit) =
        authRepository.reAuthenticate()

    override fun deleteUser(result: (NetworkResult<Unit>) -> Unit) =
        realDataBaseRepository.deleteRemoteUser {
            authRepository.deleteUser(result)
        }

    override suspend fun clearDataByKeys(keys: List<String>) =
        preferencesRepository.clearDataByKeys(keys)

    override suspend fun clearDataInDB() {
        TODO("Not yet implemented")
    }
}