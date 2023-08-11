package com.vn.talktoai.data.repositoryimpl

import com.vn.talktoai.CommonExtensions.apiCall
import com.vn.talktoai.data.database.dao.MessageDao
import com.vn.talktoai.data.database.db_entities.Message
import com.vn.talktoai.data.network.ApiService
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.repositories.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(private val messageDao: MessageDao, private val apiService: ApiService) : MessageRepository {

    override suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message)
    }

    override suspend fun getMessagesFromChat(chatId: Int): Flow<List<Message>> = messageDao.getMessagesFromChat(chatId)

    override suspend fun getCompletions(apiRequest: ApiRequest) = flow {
        emit(apiService.getCompletions(apiRequest).apiCall())
    }.flowOn(Dispatchers.IO)
}