package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun clearChats()

    fun getChatById(chatId: String): Flow<Chat?>

    fun deleteChatById(chatId: String): Flow<Unit>

    suspend fun insertChats(chats: List<Chat>)

    suspend fun insertChat(chat: Chat)

    fun getChats(): Flow<List<Chat>>

    fun getLastUpdatedChat(): Flow<Chat?>

    suspend fun updateChat(chat: Chat)

    suspend fun deleteChat(chat: Chat)

    suspend fun updateChats(chats: List<Chat>)
}