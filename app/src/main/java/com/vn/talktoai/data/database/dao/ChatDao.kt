package com.vn.talktoai.data.database.dao

import androidx.room.*
import com.vn.talktoai.data.database.db_entities.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: Chat)

    @Query("SELECT * FROM Chat")
    fun getChats(): Flow<List<Chat>>

    @Update
    fun updateChat(chat: Chat)

    @Delete
    fun deleteChat(chat: Chat)
}