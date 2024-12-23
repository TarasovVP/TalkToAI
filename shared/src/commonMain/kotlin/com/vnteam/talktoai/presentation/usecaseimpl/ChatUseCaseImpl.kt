package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.data.network.request.ApiRequest
import com.vnteam.talktoai.domain.mappers.ChatUIMapper
import com.vnteam.talktoai.domain.mappers.MessageUIMapper
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.ChatUseCase
import kotlinx.coroutines.flow.Flow

class ChatUseCaseImpl(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
    private val chatUIMapper: ChatUIMapper,
    private val messageUIMapper: MessageUIMapper
) : ChatUseCase {

    override suspend fun insertChat(chat: Chat) = chatRepository.insertChat(chat)

    override suspend fun getCurrentChat(chatId: Long): Flow<Chat?> {
        return when (chatId) {
            DEFAULT_CHAT_ID -> chatRepository.getLastUpdatedChat()
            else -> chatRepository.getChatById(chatId.toString())
        }
    }

    override fun isAuthorisedUser() = authRepository.isAuthorisedUser()

    override fun insertRemoteChat(chat: Chat, result: (NetworkResult<Unit>) -> Unit) =
        realDataBaseRepository.insertChat(chat, result)

    override fun insertRemoteMessage(message: Message, result: (NetworkResult<Unit>) -> Unit) {
        realDataBaseRepository.insertMessage(message, result)
    }

    override suspend fun insertMessage(message: Message) {
        messageRepository.insertMessage(message)
    }

    override suspend fun deleteMessages(messageIds: List<Long>) =
        messageRepository.deleteMessages(messageIds)

    override fun deleteRemoteMessages(
        messageIds: List<Long>,
        result: (NetworkResult<Unit>) -> Unit
    ) = realDataBaseRepository.deleteMessages(messageIds.map { it.toString() }, result)

    override suspend fun getMessagesFromChat(chatId: Long): Flow<List<Message>> =
        messageRepository.getMessagesFromChat(chatId)

    override suspend fun sendRequest(apiRequest: ApiRequest) =
        messageRepository.sendRequest(apiRequest)

}