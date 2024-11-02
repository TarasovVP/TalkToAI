package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ChatDBRepository {

    suspend fun clearChats()

    suspend fun insertChatsToDB(chats: List<Chat>): Flow<Unit>

    suspend fun getChatsFromDB(): Flow<List<Chat>>

    suspend fun getChatById(chatId: String): Flow<Chat?>

    suspend fun deleteChatById(chatId: String): Flow<Unit>
}