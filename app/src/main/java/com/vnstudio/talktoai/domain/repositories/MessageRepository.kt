package com.vnstudio.talktoai.domain.repositories

import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.ApiResponse
import com.vnstudio.talktoai.domain.sealed_classes.Result
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun insertMessages(messages: List<Message>)

    suspend fun insertMessage(message: Message)

    suspend fun updateMessage(message: Message)

    suspend fun getMessages(): Flow<List<Message>>

    suspend fun getMessagesFromChat(chatId: Long): Flow<List<Message>>

    suspend fun deleteMessage(id: Long)

    suspend fun deleteMessagesFromChat(chatId: Long)

    suspend fun sendRequest(apiRequest: ApiRequest): Flow<Result<ApiResponse>>
}