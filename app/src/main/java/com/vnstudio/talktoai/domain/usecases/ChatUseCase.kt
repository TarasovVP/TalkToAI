package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.ApiResponse
import com.vnstudio.talktoai.domain.sealed_classes.Result
import kotlinx.coroutines.flow.Flow

interface ChatUseCase {

    suspend fun insertChat(chat: Chat)

    suspend fun getCurrentChat(): Flow<Chat?>

    suspend fun insertMessage(message: Message)

    suspend fun updateMessage(message: Message)

    suspend fun getMessagesFromChat(chatId: Int): Flow<List<Message>>

    suspend fun sendRequest(apiRequest: ApiRequest): Flow<Result<ApiResponse>>
}