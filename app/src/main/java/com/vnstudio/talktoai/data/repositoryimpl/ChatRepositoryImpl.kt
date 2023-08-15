package com.vnstudio.talktoai.data.repositoryimpl

import android.util.Log
import com.vnstudio.talktoai.data.database.dao.ChatDao
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(private val chatDao: ChatDao) : ChatRepository {
    override suspend fun insertChat(chat: Chat) = chatDao.insertChat(chat)

    override suspend fun getChats(): Flow<List<Chat>> = chatDao.getChats()

    override suspend fun getCurrentChat(): Flow<Chat?> = chatDao.getCurrentChat()

    override suspend fun updateChat(chat: Chat) = chatDao.updateChat(chat)

    override suspend fun deleteChat(chat: Chat) = chatDao.deleteChat(chat)

    override suspend fun updateChats(chats: List<Chat>) {
        chatDao.updateChats(chats)
        Log.e(
            "apiTAG",
            "ChatRepositoryImpl updateChats $chats "
        )
    }
}