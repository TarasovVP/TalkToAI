package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.database.dao.ChatDao
import com.vnteam.talktoai.domain.mappers.ChatDBMapper
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.repositories.ChatDBRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ChatDBRepositoryImpl(private val chatDao: ChatDao, private val chatDBMapper: ChatDBMapper):
    ChatDBRepository {

    override suspend fun clearChats() {
        chatDao.clearChats()
    }

    override suspend fun insertChatsToDB(chats: List<Chat>): Flow<Unit> = flow {
        chatDao.insertChats(chatDBMapper.mapToImplModelList(chats))
        emit(Unit)
    }

    override suspend fun getChatsFromDB(): Flow<List<Chat>> =
        chatDao.getChats().map { demoObjectWithOwners ->
            chatDBMapper.mapFromImplModelList(demoObjectWithOwners)
        }


    override suspend fun getChatById(chatId: String): Flow<Chat?> =
        chatDao.getChatById(chatId.toLong()).map { chat ->
            chat?.let { chatDBMapper.mapFromImplModel(it) }
        }

    override suspend fun deleteChatById(chatId: String): Flow<Unit> = flow {
        chatDao.deleteChatById(chatId.toLong())
        emit(Unit)
    }
}