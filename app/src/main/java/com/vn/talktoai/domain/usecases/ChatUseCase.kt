package com.vn.talktoai.domain.usecases

import com.vn.talktoai.data.database.db_entities.Message
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import kotlinx.coroutines.flow.Flow
import com.vn.talktoai.data.network.Result

interface ChatUseCase {

    suspend fun insertMessage(message: Message)

    suspend fun getMessagesFromChat(chatId: Int): Flow<List<Message>>

    suspend fun sendRequest(apiRequest: ApiRequest): Flow<Result<ApiResponse>>
}