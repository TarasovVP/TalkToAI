package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages

import com.vnteam.talktoai.CommonExtensions.getUserAuth
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.enums.isAuthorisedUser
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.firstOrNull

class InsertMessageUseCase(
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository,
    private val messageRepository: MessageRepository,
    private val remoteStoreRepository: RemoteStoreRepository,
) : UseCase<Message, Result<Unit>> {

    override suspend fun execute(params: Message): Result<Unit> {
        messageRepository.insertMessage(params)
        val userAuth = preferencesRepository.getUserEmail().firstOrNull()
        val authState = userAuth.getUserAuth()
        if (authState.isAuthorisedUser() && networkState.isNetworkAvailable()) {
            remoteStoreRepository.insertMessage(params).firstOrNull()
        }
        return Result.Success(Unit)
    }
}
