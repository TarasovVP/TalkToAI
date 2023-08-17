package com.vnstudio.talktoai.presentation.chat

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.repositories.ChatRepository
import com.vnstudio.talktoai.domain.repositories.MessageRepository
import com.vnstudio.talktoai.domain.usecases.ChatUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatUseCaseImpl @Inject constructor(private val chatRepository: ChatRepository, private val messageRepository: MessageRepository) : ChatUseCase {

    override suspend fun insertChat(chat: Chat) = chatRepository.insertChat(chat)

    override suspend fun getCurrentChat(): Flow<Chat?> = chatRepository.getCurrentChat()

    override suspend fun insertMessage(message: Message) = messageRepository.insertMessage(message)

    override suspend fun updateMessage(message: Message) = messageRepository.updateMessage(message)

    override suspend fun getMessagesFromChat(chatId: Int) = messageRepository.getMessagesFromChat(chatId)

    override suspend fun sendRequest(apiRequest: ApiRequest) = messageRepository.sendRequest(apiRequest)

}