package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.data.network.request.ApiRequest
import com.vnteam.talktoai.data.network.responses.ApiResponse
import com.vnteam.talktoai.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun insertMessages(messages: List<Message>)

    suspend fun insertMessage(message: Message)

    suspend fun getMessages(): Flow<NetworkResult<List<Message>>>

    suspend fun getMessagesFromChat(chatId: Long): Flow<NetworkResult<List<Message>>>

    suspend fun deleteMessage(id: Long)

    suspend fun deleteMessages(messageIds: List<Long>)

    suspend fun deleteMessagesFromChat(chatId: Long)

    suspend fun updateMessages(messages: List<Message>)

    suspend fun sendRequest(apiRequest: ApiRequest): Flow<NetworkResult<NetworkResult<ApiResponse>>>
}