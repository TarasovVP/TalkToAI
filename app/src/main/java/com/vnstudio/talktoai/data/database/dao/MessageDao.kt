package com.vnstudio.talktoai.data.database.dao

import androidx.room.*
import com.vnstudio.talktoai.data.database.db_entities.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: Message)

    @Update
    fun updateMessage(message: Message)

    @Query("SELECT * FROM messages")
    fun getMessages(): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE :chatId = chatId")
    fun getMessagesFromChat(chatId: Int): Flow<List<Message>>

    @Query("DELETE FROM messages WHERE :chatId = chatId")
    fun deleteMessagesFromChat(chatId: Int)
}