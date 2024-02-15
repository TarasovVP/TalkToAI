package com.vnstudio.talktoai

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vnstudio.talktoai.data.database.dao.ChatDao
import com.vnstudio.talktoai.data.database.db_entities.Chat
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ChatDaoTest {

    private lateinit var chatDao: ChatDao
    private lateinit var database: AppDatabase
    private val chat1 = Chat(1, "Chat1", 100L, 0)
    private val chat2 = Chat(2, "Chat2", 200L, 1)

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        chatDao = database.chatDao()
    }

    @Test
    @Throws(Exception::class)
    fun checkChatInsert() = runBlocking {
        chatDao.insertChat(chat1)
        val allChats = chatDao.getChats()
        assertEquals(allChats.firstOrNull()?.firstOrNull(), chat1)
    }

    @Test
    @Throws(Exception::class)
    fun checkGetChatById() = runBlocking {
        chatDao.insertChats(listOf(chat1))
        val chat = chatDao.getChatById(chat1.id)
        assertEquals(chat.firstOrNull(), chat1)
    }

    @Test
    @Throws(Exception::class)
    fun checkChatsInsert() = runBlocking {
        chatDao.insertChats(listOf(chat1, chat2))
        val chat = chatDao.getChatById(chat1.id)
        assertEquals(chat.firstOrNull(), chat1)
    }

    @Test
    @Throws(Exception::class)
    fun checkChatsDelete() = runBlocking {
        chatDao.insertChats(listOf(chat1))
        val allChats = chatDao.getChats()
        assertTrue(allChats.firstOrNull().isNullOrEmpty().not())
        chatDao.deleteChat(chat1)
        chatDao.deleteChat(chat2)
        assertTrue(allChats.firstOrNull().isNullOrEmpty())
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }
}
