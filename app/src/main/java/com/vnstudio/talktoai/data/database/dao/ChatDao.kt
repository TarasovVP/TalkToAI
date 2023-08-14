package com.vnstudio.talktoai.data.database.dao

import androidx.room.*
import com.vnstudio.talktoai.data.database.db_entities.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: Chat)

    @Update
    fun updateChats(chats: List<Chat>)

    @Query("SELECT * FROM Chat ORDER BY updated DESC")
    fun getChats(): Flow<List<Chat>>

    @Update
    fun updateChat(chat: Chat)

    @Delete
    fun deleteChat(chat: Chat)
}