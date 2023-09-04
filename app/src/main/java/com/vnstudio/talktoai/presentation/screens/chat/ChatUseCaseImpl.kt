package com.vnstudio.talktoai.presentation.screens.chat

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.repositories.AuthRepository
import com.vnstudio.talktoai.domain.repositories.ChatRepository
import com.vnstudio.talktoai.domain.repositories.MessageRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.ChatUseCase
import com.vnstudio.talktoai.domain.sealed_classes.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatUseCaseImpl @Inject constructor(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository
) : ChatUseCase {

    override suspend fun insertChat(chat: Chat) = chatRepository.insertChat(chat)

    override suspend fun getCurrentChat(): Flow<Chat?> = chatRepository.getCurrentChat()

    override fun isAuthorisedUser() = authRepository.isAuthorisedUser()

    override fun insertRemoteChat(chat: Chat, result: (Result<Unit>) -> Unit) = realDataBaseRepository.insertChat(chat, result)

    override fun insertRemoteMessage(message: Message, result: (Result<Unit>) -> Unit) = realDataBaseRepository.insertMessage(message, result)

    override suspend fun insertMessage(message: Message) = messageRepository.insertMessage(message)

    override suspend fun deleteMessage(id: Long) = messageRepository.deleteMessage(id)

    override suspend fun getMessagesFromChat(chatId: Long) = messageRepository.getMessagesFromChat(chatId)

    override suspend fun sendRequest(apiRequest: ApiRequest) = messageRepository.sendRequest(apiRequest)

}