package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun clearChats()

    suspend fun getChatById(chatId: String): Flow<Chat?>

    suspend fun insertChats(chats: List<Chat>)

    suspend fun insertChat(chat: Chat)

    suspend fun getChats(): Flow<List<Chat>>

    suspend fun getLastUpdatedChat(): Flow<Chat?>

    suspend fun updateChat(chat: Chat)

    suspend fun deleteChat(chat: Chat)

    suspend fun updateChats(chats: List<Chat>)
}