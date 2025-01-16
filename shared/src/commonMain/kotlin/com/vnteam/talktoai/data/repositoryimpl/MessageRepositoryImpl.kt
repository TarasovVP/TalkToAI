package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.CommonExtensions.orZero
import com.vnteam.talktoai.data.database.dao.MessageDao
import com.vnteam.talktoai.domain.mappers.MessageDBMapper
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.repositories.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(
    private val messageDao: MessageDao,
    private val messageDBMapper: MessageDBMapper
) : MessageRepository {

    override suspend fun insertMessages(messages: List<Message>) {
        val messageDBs = messages.map { messageDBMapper.mapToImplModel(it) }
        messageDao.insertMessages(messageDBs)
    }

    override suspend fun insertMessage(message: Message) {
        val messageDB = messageDBMapper.mapToImplModel(message)
        messageDao.insertMessage(messageDB)
    }

    override suspend fun getMessages(): Flow<List<Message>> {
        return messageDao.getMessages().map { messages ->
            messages.map { messageDBMapper.mapFromImplModel(it) }
        }
    }

    override suspend fun getMessagesFromChat(chatId: Long): Flow<List<Message>> {
        return messageDao.getMessagesFromChat(chatId).map { messages ->
            messages.map { messageDBMapper.mapFromImplModel(it) }
        }
    }

    override suspend fun deleteMessage(id: Long) = messageDao.deleteMessage(id)

    override suspend fun deleteMessagesFromChat(chatId: Long) =
        messageDao.deleteMessagesFromChat(chatId)

    override suspend fun deleteMessages(messageIds: List<Long>) =
        messageDao.deleteMessages(messageIds)

    override suspend fun updateMessages(messages: List<Message>) {
        deleteMessages(messages.map { it.id.orZero() })
        insertMessages(messages)
    }
}