package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import kotlinx.coroutines.flow.firstOrNull

class SyncRemoteUserUseCase(
    private val remoteStoreRepository: RemoteStoreRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    suspend fun execute(): Result<RemoteUser> {
        return when (val result = remoteStoreRepository.getRemoteUser().firstOrNull()) {
            is Result.Success -> {
                val remoteUser = result.data ?: RemoteUser()
                messageRepository.clearMessages()
                chatRepository.clearChats()
                chatRepository.insertChats(remoteUser.chats)
                messageRepository.insertMessages(remoteUser.messages)
                syncSettings()
                Result.Success(remoteUser)
            }

            is Result.Failure -> result
            else -> Result.Failure("Remote sync failed")
        }
    }

    private suspend fun syncSettings() {
        val result = remoteStoreRepository.getRemoteSettings().firstOrNull()
        if (result is Result.Success) {
            val settings = result.data ?: return
            settings["aiModel"]?.takeIf { it.isNotEmpty() }?.let { preferencesRepository.setAiModel(it) }
            settings["apiKey"]?.takeIf { it.isNotEmpty() }?.let { preferencesRepository.setApiKey(it) }
            settings["temperature"]?.takeIf { it.isNotEmpty() }?.let { preferencesRepository.setTemperature(it) }
            settings["globalContext"]?.let { preferencesRepository.setGlobalSystemContext(it) }
        }
    }
}
