package com.vn.talktoai.domain.repositories

import com.vn.talktoai.data.database.db_entities.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun insertChat(chat: Chat)

    suspend fun getChats(): Flow<List<Chat>>

    suspend fun updateChat(chat: Chat)

    suspend fun deleteChat(chat: Chat)
}