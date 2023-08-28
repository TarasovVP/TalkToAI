package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.ApiResponse
import kotlinx.coroutines.flow.Flow
import com.vnstudio.talktoai.domain.sealed_classes.Result

interface ChatUseCase {

    suspend fun getChats(): Flow<List<Chat>>

    suspend fun updateChat(chat: Chat)

    suspend fun deleteChat(chat: Chat)

    suspend fun deleteMessagesFromChat(chatId: Int)

    suspend fun updateChats(chats: List<Chat>)

    suspend fun insertChat(chat: Chat)

    suspend fun getCurrentChat(): Flow<Chat?>

    suspend fun insertMessage(message: Message)

    suspend fun updateMessage(message: Message)

    suspend fun getMessagesFromChat(chatId: Int): Flow<List<Message>>

    suspend fun sendRequest(apiRequest: ApiRequest): Flow<Result<ApiResponse>>
}