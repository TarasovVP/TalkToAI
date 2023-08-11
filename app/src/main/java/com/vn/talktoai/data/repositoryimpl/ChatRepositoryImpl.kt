package com.vn.talktoai.data.repositoryimpl

import com.vn.talktoai.data.database.dao.ChatDao
import com.vn.talktoai.data.database.db_entities.Chat
import com.vn.talktoai.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(private val chatDao: ChatDao) : ChatRepository {
    override suspend fun insertChat(chat: Chat) {
        chatDao.insertChat(chat)
    }

    override suspend fun getChats(): Flow<List<Chat>> = chatDao.getChats()
}