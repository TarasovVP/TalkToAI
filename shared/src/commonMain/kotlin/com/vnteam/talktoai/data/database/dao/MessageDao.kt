package com.vnteam.talktoai.data.database.dao

import com.vnteam.talktoai.MessageDB
import kotlinx.coroutines.flow.Flow

interface MessageDao {

    suspend fun insertMessages(messages: List<MessageDB>)

    suspend fun insertMessage(message: MessageDB)

    suspend fun getMessages(): Flow<List<MessageDB>>

    suspend fun getMessagesFromChat(chatId: Long): Flow<List<MessageDB>>

    suspend fun deleteMessagesFromChat(chatId: Long)

    suspend fun deleteMessage(id: Long)

    suspend fun deleteMessages(messageIds: List<Long>)
}