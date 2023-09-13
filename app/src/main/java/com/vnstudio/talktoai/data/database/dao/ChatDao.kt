package com.vnstudio.talktoai.data.database.dao

import androidx.room.*
import com.vnstudio.talktoai.data.database.db_entities.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChats(chats: List<Chat>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: Chat)

    @Query("DELETE FROM chats WHERE id NOT IN (:chatIds)")
    fun deleteChats(chatIds: List<Long>)

    @Query("SELECT * FROM chats ORDER BY listOrder DESC")
    fun getChats(): Flow<List<Chat>>

    @Query("SELECT * FROM chats ORDER BY updated DESC LIMIT 1")
    fun getLastUpdatedChat(): Flow<Chat?>

    @Query("SELECT * FROM chats WHERE :chatId = id")
    fun getChatById(chatId: Long): Flow<Chat?>

    @Update
    fun updateChat(chat: Chat)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateChats(chats: List<Chat>)

    @Delete
    fun deleteChat(chat: Chat)
}