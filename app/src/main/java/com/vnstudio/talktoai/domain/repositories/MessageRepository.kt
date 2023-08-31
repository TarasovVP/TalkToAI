package com.vnstudio.talktoai.domain.repositories

import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.ApiResponse
import com.vnstudio.talktoai.domain.sealed_classes.Result
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun insertMessage(message: Message)

    suspend fun updateMessage(message: Message)

    suspend fun getMessagesFromChat(chatId: Int): Flow<List<Message>>

    suspend fun deleteMessagesFromChat(chatId: Int)

    suspend fun sendRequest(apiRequest: ApiRequest): Flow<Result<ApiResponse>>
}