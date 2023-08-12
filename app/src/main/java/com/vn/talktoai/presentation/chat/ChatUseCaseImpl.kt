package com.vn.talktoai.presentation.chat

import com.vn.talktoai.data.database.db_entities.Message
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.repositories.MessageRepository
import com.vn.talktoai.domain.usecases.ChatUseCase
import javax.inject.Inject

class ChatUseCaseImpl @Inject constructor(private val messageRepository: MessageRepository) : ChatUseCase {

    override suspend fun insertMessage(message: Message) = messageRepository.insertMessage(message)

    override suspend fun getMessagesFromChat(chatId: Int) = messageRepository.getMessagesFromChat(chatId)

    override suspend fun sendRequest(apiRequest: ApiRequest) = messageRepository.sendRequest(apiRequest)

}