package com.vnteam.talktoai.data.database.dao

import com.vnteam.talktoai.ChatDB
import kotlinx.coroutines.flow.Flow

interface ChatDao {

    suspend fun clearChats()

    suspend fun insertChats(chats: List<ChatDB>)

    suspend fun insertChat(chat: ChatDB)

    suspend fun deleteChats(chatIds: List<Long>)

    suspend fun getChats(): Flow<List<ChatDB>>

    suspend fun getLastUpdatedChat(): Flow<ChatDB?>

    suspend fun getChatById(chatId: Long): Flow<ChatDB?>

    suspend fun updateChat(chat: ChatDB)

    suspend fun updateChats(chats: List<ChatDB>)

    suspend fun deleteChat(chat: ChatDB)

    suspend fun deleteChatById(id: Long)

}