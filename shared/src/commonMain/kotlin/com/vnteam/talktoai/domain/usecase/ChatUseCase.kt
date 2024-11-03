package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.data.network.request.ApiRequest
import com.vnteam.talktoai.data.network.responses.ApiResponse
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatUseCase {

    suspend fun insertChat(chat: Chat)

    suspend fun getCurrentChat(chatId: Long): Flow<Chat?>

    fun isAuthorisedUser(): Boolean

    fun insertRemoteChat(chat: Chat, result: (NetworkResult<Unit>) -> Unit)

    suspend fun insertMessage(message: Message)

    fun insertRemoteMessage(message: Message, result: (NetworkResult<Unit>) -> Unit)

    suspend fun deleteMessages(messageIds: List<Long>)

    fun deleteRemoteMessages(messageIds: List<Long>, result: (NetworkResult<Unit>) -> Unit)

    suspend fun getMessagesFromChat(chatId: Long): Flow<List<Message>>

    suspend fun sendRequest(apiRequest: ApiRequest): Flow<NetworkResult<ApiResponse>>
}