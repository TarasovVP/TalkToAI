package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.SettingsAccountUseCase
import kotlinx.coroutines.flow.Flow

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

    override fun signOut(): Flow<NetworkResult<Unit>> =
        authRepository.signOut()

    override fun changePassword(
        currentPassword: String,
        newPassword: String): Flow<NetworkResult<Unit>> = authRepository.changePassword(currentPassword, newPassword)

    override fun reAuthenticate(/*authCredential: AuthCredential, */): Flow<NetworkResult<Unit>> =
        authRepository.reAuthenticate()

    override fun deleteUser(): Flow<NetworkResult<Unit>> =
        realDataBaseRepository.deleteRemoteUser()/* {
            authRepository.deleteUser(result)
        }*/

    override suspend fun clearDataByKeys(keys: List<String>) =
        preferencesRepository.clearDataByKeys(keys)

    override suspend fun clearDataInDB() {
        TODO("Not yet implemented")
    }

    override fun googleSignIn() {
        authRepository.googleSignIn()
    }
}