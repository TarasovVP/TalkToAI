package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.request.ApiRequest
import com.vnteam.talktoai.data.network.responses.ApiResponse
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatUseCase {

    suspend fun insertChat(chat: Chat)

    suspend fun getCurrentChat(chatId: Long): Flow<Result<Chat?>>

    fun isAuthorisedUser(): Boolean

    fun insertRemoteChat(chat: Chat): Flow<Result<Unit>>

    suspend fun insertMessage(message: Message)

    fun insertRemoteMessage(message: Message): Flow<Result<Unit>>

    suspend fun deleteMessages(messageIds: List<Long>)

    fun deleteRemoteMessages(messageIds: List<Long>): Flow<Result<Unit>>

    suspend fun getMessagesFromChat(chatId: Long): Flow<Result<List<Message>>>

    suspend fun sendRequest(apiRequest: ApiRequest): Flow<Result<ApiResponse>>
}