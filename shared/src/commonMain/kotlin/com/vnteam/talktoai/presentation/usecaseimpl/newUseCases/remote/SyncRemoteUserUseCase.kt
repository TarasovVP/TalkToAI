package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.MessageContent
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import kotlinx.coroutines.flow.firstOrNull

class SyncRemoteUserUseCase(
    private val remoteStoreRepository: RemoteStoreRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val syncRemoteSettingsUseCase: SyncRemoteSettingsUseCase,
) {

    suspend fun execute(): Result<RemoteUser> {
        return when (val result = remoteStoreRepository.getRemoteUser().firstOrNull()) {
            is Result.Success -> {
                val remoteUser = result.data ?: RemoteUser()
                // Remote message documents never carry local-only image data (storageKey is
                // never written to or read from Firestore), so a full resync would otherwise
                // silently wipe the locally attached image for every message that has one.
                val localImagesByMessageId = messageRepository.getMessages().firstOrNull().orEmpty()
                    .mapNotNull { message ->
                        message.content.filterIsInstance<MessageContent.Image>()
                            .firstOrNull { it.storageKey != null }
                            ?.let { message.id to it }
                    }
                    .toMap()
                messageRepository.clearMessages()
                chatRepository.clearChats()
                chatRepository.insertChats(remoteUser.chats)
                val mergedMessages = remoteUser.messages.map { message ->
                    val localImage = localImagesByMessageId[message.id] ?: return@map message
                    message.copy(content = message.content + localImage)
                }
                messageRepository.insertMessages(mergedMessages)
                syncRemoteSettingsUseCase.execute()
                Result.Success(remoteUser)
            }

            is Result.Failure -> result
            else -> Result.Failure("Remote sync failed")
        }
    }
}
