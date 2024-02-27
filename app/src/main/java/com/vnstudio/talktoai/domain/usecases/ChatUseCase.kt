package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.data.network.models.ApiRequest
import com.vnstudio.talktoai.data.network.models.ApiResponse
import com.vnstudio.talktoai.domain.sealed_classes.Result
import kotlinx.coroutines.flow.Flow

interface ChatUseCase {

    suspend fun insertChat(chat: Chat)

    suspend fun getCurrentChat(chatId: Long): Flow<Chat?>

    fun isAuthorisedUser(): Boolean

    fun insertRemoteChat(chat: Chat, result: (Result<Unit>) -> Unit)

    suspend fun insertMessage(message: Message)

    fun insertRemoteMessage(message: Message, result: (Result<Unit>) -> Unit)

    suspend fun deleteMessages(messageIds: List<Long>)

    fun deleteRemoteMessages(messageIds: List<Long>, result: (Result<Unit>) -> Unit)

    suspend fun getMessagesFromChat(chatId: Long): Flow<List<Message>>

    suspend fun sendRequest(apiRequest: ApiRequest): Flow<Result<ApiResponse>>
}