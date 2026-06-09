package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import kotlinx.coroutines.flow.firstOrNull

class SyncRemoteUserUseCase(
    private val remoteStoreRepository: RemoteStoreRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
) {

    suspend fun execute(): Result<RemoteUser> {
        return when (val result = remoteStoreRepository.getRemoteUser().firstOrNull()) {
            is Result.Success -> {
                val remoteUser = result.data ?: RemoteUser()
                messageRepository.clearMessages()
                chatRepository.clearChats()
                chatRepository.insertChats(remoteUser.chats)
                messageRepository.insertMessages(remoteUser.messages)
                Result.Success(remoteUser)
            }

            is Result.Failure -> result
            else -> Result.Failure("Remote sync failed")
        }
    }
}
