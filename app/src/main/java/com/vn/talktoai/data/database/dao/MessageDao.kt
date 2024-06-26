package com.vn.talktoai.data.database.dao

import androidx.room.*
import com.vn.talktoai.data.database.db_entities.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: Message)

    @Query("SELECT * FROM Message WHERE :chatId = chatId")
    fun getMessagesFromChat(chatId: Int): Flow<List<Message>>
}