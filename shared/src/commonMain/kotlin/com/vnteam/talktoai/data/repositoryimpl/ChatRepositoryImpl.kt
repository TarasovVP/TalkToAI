package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.database.dao.ChatDao
import com.vnteam.talktoai.domain.mappers.ChatDBMapper
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(private val chatDao: ChatDao, private val chatDBMapper: ChatDBMapper) :
    ChatRepository {

    override suspend fun clearChats() {
        chatDao.clearChats()
    }

    override suspend fun insertChats(chats: List<Chat>) {
        chatDao.insertChats(chatDBMapper.mapToImplModelList(chats))
    }

    override suspend fun insertChat(chat: Chat) {
        chatDao.insertChat(chatDBMapper.mapToImplModel(chat))
    }

    override suspend fun getChats(): Flow<List<Chat>> =
        chatDao.getChats().map { demoObjectWithOwners ->
            chatDBMapper.mapFromImplModelList(demoObjectWithOwners)
        }

    override suspend fun getLastUpdatedChat(): Flow<Chat?> {
        return chatDao.getLastUpdatedChat().map { chat ->
            chat?.let { chatDBMapper.mapFromImplModel(it) }
        }
    }

    override suspend fun updateChat(chat: Chat) {
        chatDao.updateChat(chatDBMapper.mapToImplModel(chat))
    }

    override suspend fun deleteChat(chat: Chat) {
        chatDao.deleteChat(chatDBMapper.mapToImplModel(chat))
    }

    override suspend fun updateChats(chats: List<Chat>) {
        chatDao.updateChats(chatDBMapper.mapToImplModelList(chats))
    }

    override suspend fun getChatById(chatId: String): Flow<Chat?> =
        chatDao.getChatById(chatId.toLong()).map { chat ->
            chat?.let { chatDBMapper.mapFromImplModel(it) }
        }
}