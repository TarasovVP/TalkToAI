package com.vnstudio.talktoai.data.repositoryimpls

import com.vnstudio.talktoai.data.database.dao.ChatDao
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow


class ChatRepositoryImpl(private val chatDao: ChatDao) : ChatRepository {

    override suspend fun insertChats(chats: List<Chat>) = chatDao.insertChats(chats)

    override suspend fun insertChat(chat: Chat) = chatDao.insertChat(chat)

    override suspend fun getChats(): Flow<List<Chat>> = chatDao.getChats()

    override suspend fun getLastUpdatedChat() = chatDao.getLastUpdatedChat()

    override suspend fun getChatById(chatId: Long) = chatDao.getChatById(chatId)

    override suspend fun updateChat(chat: Chat) = chatDao.updateChat(chat)

    override suspend fun deleteChat(chat: Chat) = chatDao.deleteChat(chat)

    override suspend fun updateChats(chats: List<Chat>) {
        chatDao.deleteChats(chats.map { it.id })
        chatDao.insertChats(chats)
    }

    override suspend fun clearChats() = chatDao.clearChats()
}