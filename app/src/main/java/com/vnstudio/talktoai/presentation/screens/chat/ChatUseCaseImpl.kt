package com.vnstudio.talktoai.presentation.screens.chat

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.mappers.MessageUIMapper
import com.vnstudio.talktoai.domain.repositories.AuthRepository
import com.vnstudio.talktoai.domain.repositories.ChatRepository
import com.vnstudio.talktoai.domain.repositories.MessageRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.ChatUseCase
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.presentation.ui_models.MessageUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatUseCaseImpl @Inject constructor(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
    private val messageUIMapper: MessageUIMapper
) : ChatUseCase {

    override suspend fun insertChat(chat: Chat) = chatRepository.insertChat(chat)

    override suspend fun getCurrentChat(chatId: Long): Flow<Chat?> {
        return when (chatId) {
            DEFAULT_CHAT_ID -> chatRepository.getLastUpdatedChat()
            else -> chatRepository.getChatById(chatId)
        }
    }

    override fun isAuthorisedUser() = authRepository.isAuthorisedUser()

    override fun insertRemoteChat(chat: Chat, result: (Result<Unit>) -> Unit) = realDataBaseRepository.insertChat(chat, result)

    override fun insertRemoteMessage(messageUIModel: MessageUIModel, result: (Result<Unit>) -> Unit) {
        realDataBaseRepository.insertMessage(messageUIMapper.mapFromUIModel(messageUIModel), result)
    }

    override suspend fun insertMessage(messageUIModel: MessageUIModel) {
        messageRepository.insertMessage(messageUIMapper.mapFromUIModel(messageUIModel))
    }

    override suspend fun deleteMessage(id: Long) = messageRepository.deleteMessage(id)

    override suspend fun getMessagesFromChat(chatId: Long): Flow<List<MessageUIModel>> {
        return messageRepository.getMessagesFromChat(chatId).map { messages ->
            messageUIMapper.mapToUIModelList(messages)
        }
    }

    override suspend fun sendRequest(apiRequest: ApiRequest) = messageRepository.sendRequest(apiRequest)

}