package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.data.network.Result
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

    override suspend fun getCurrentChat(chatId: Long): Flow<Result<Chat?>> {
        return when (chatId) {
            DEFAULT_CHAT_ID -> chatRepository.getLastUpdatedChat()
            else -> chatRepository.getChatById(chatId.toString())
        }
    }

    override fun insertRemoteChat(chat: Chat): Flow<Result<Unit>> =
        realDataBaseRepository.insertChat(chat)

    override fun insertRemoteMessage(message: Message): Flow<Result<Unit>> {
        return realDataBaseRepository.insertMessage(message)
    }

    override suspend fun insertMessage(message: Message) {
        messageRepository.insertMessage(message)
    }

    override suspend fun deleteMessages(messageIds: List<Long>) =
        messageRepository.deleteMessages(messageIds)

    override fun deleteRemoteMessages(
        messageIds: List<Long>): Flow<Result<Unit>> =
        realDataBaseRepository.deleteMessages(messageIds.map { it.toString() })

    override suspend fun getMessagesFromChat(chatId: Long): Flow<Result<List<Message>>> =
        messageRepository.getMessagesFromChat(chatId)

    override suspend fun sendRequest(apiRequest: ApiRequest) =
        messageRepository.sendRequest(apiRequest)

}