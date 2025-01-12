package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun clearChats()

    suspend fun getChatById(chatId: String): Flow<Result<Chat?>>

    suspend fun deleteChatById(chatId: String): Flow<Result<Unit>>

    suspend fun insertChats(chats: List<Chat>)

    suspend fun insertChat(chat: Chat)

    suspend fun getChats(): Flow<Result<List<Chat>>>

    suspend fun getLastUpdatedChat(): Flow<Result<Chat?>>

    suspend fun updateChat(chat: Chat)

    suspend fun deleteChat(chat: Chat)

    suspend fun updateChats(chats: List<Chat>)
}