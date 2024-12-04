package com.vnstudio.talktoai.data.repositoryimpls

import com.vnstudio.talktoai.CommonExtensions.handleResponse
import com.vnstudio.talktoai.CommonExtensions.orZero
import com.vnstudio.talktoai.data.database.dao.MessageDao
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.data.network.ApiService
import com.vnstudio.talktoai.data.network.models.ApiRequest
import com.vnstudio.talktoai.data.network.models.ApiResponse
import com.vnstudio.talktoai.domain.repositories.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class MessageRepositoryImpl(
    private val messageDao: MessageDao,
    private val apiService: ApiService,
) : MessageRepository {

    override suspend fun insertMessages(messages: List<Message>) =
        messageDao.insertMessages(messages)

    override suspend fun insertMessage(message: Message) = messageDao.insertMessage(message)

    override suspend fun getMessages(): Flow<List<Message>> = messageDao.getMessages()

    override suspend fun getMessagesFromChat(chatId: Long): Flow<List<Message>> =
        messageDao.getMessagesFromChat(chatId)

    override suspend fun deleteMessage(id: Long) = messageDao.deleteMessage(id)

    override suspend fun deleteMessagesFromChat(chatId: Long) =
        messageDao.deleteMessagesFromChat(chatId)

    override suspend fun deleteMessages(messageIds: List<Long>) =
        messageDao.deleteMessages(messageIds)

    override suspend fun updateMessages(messages: List<Message>) {
        deleteMessages(messages.map { it.id.orZero() })
        insertMessages(messages)
    }

    override suspend fun sendRequest(apiRequest: ApiRequest) = flow {
        val httpResponse = apiService.sendRequest(apiRequest)
        emit(httpResponse.handleResponse<ApiResponse>())
    }.flowOn(Dispatchers.IO)

    override suspend fun clearMessages() = messageDao.clearMessages()
}