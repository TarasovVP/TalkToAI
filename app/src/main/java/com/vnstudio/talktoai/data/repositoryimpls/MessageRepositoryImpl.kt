package com.vnstudio.talktoai.data.repositoryimpls

import com.vnstudio.talktoai.CommonExtensions.apiCall
import com.vnstudio.talktoai.CommonExtensions.orZero
import com.vnstudio.talktoai.data.database.dao.MessageDao
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.data.network.ApiService
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.repositories.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val apiService: ApiService,
) : MessageRepository {

    override suspend fun insertMessages(messages: List<Message>) = messageDao.insertMessages(messages)

    override suspend fun insertMessage(message: Message) = messageDao.insertMessage(message)

    override suspend fun getMessages(): Flow<List<Message>> = messageDao.getMessages()

    override suspend fun getMessagesFromChat(chatId: Long): Flow<List<Message>> = messageDao.getMessagesFromChat(chatId)

    override suspend fun deleteMessage(id: Long) = messageDao.deleteMessage(id)

    override suspend fun deleteMessagesFromChat(chatId: Long) = messageDao.deleteMessagesFromChat(chatId)

    override suspend fun updateMessages(messages: List<Message>) {
        messageDao.deleteMissingMessages(messages.map { it.id.orZero() })
        messageDao.insertMessages(messages)
    }

    override suspend fun sendRequest(apiRequest: ApiRequest) = flow {
        emit(apiService.sendRequest(apiRequest).apiCall())
    }.flowOn(Dispatchers.IO)
}