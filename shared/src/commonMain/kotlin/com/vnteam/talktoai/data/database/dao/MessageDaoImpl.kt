package com.vnteam.talktoai.data.database.dao

import app.cash.sqldelight.coroutines.asFlow
import com.vnteam.talktoai.MessageDB
import com.vnteam.talktoai.data.database.SharedDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageDaoImpl(private val appDatabase: SharedDatabase) : MessageDao {

    override suspend fun insertMessages(messages: List<MessageDB>) {
        appDatabase { db ->
            db.appDatabaseQueries.transaction {
                messages.forEach { message ->
                    db.appDatabaseQueries.insertMessage(
                        message.id,
                        message.chatId,
                        message.author,
                        message.message,
                        message.updatedAt,
                        message.status,
                        message.errorMessage,
                        message.truncated
                    )
                }
            }
        }
    }

    override suspend fun insertMessage(message: MessageDB) {
        appDatabase { db ->
            db.appDatabaseQueries.insertMessage(
                message.id,
                message.chatId,
                message.author,
                message.message,
                message.updatedAt,
                message.status,
                message.errorMessage,
                message.truncated
            )
        }
    }

    override suspend fun deleteMessages(messageIds: List<Long>) {
        appDatabase { db ->
            db.appDatabaseQueries.transaction {
                messageIds.forEach { messageId ->
                    db.appDatabaseQueries.deleteMessage(messageId)
                }
            }
        }
    }

    override suspend fun getMessages(): Flow<List<MessageDB>> {
        return appDatabase { db ->
            db.appDatabaseQueries.getMessages().asFlow().map {
                it.executeAsList().map { message ->
                    MessageDB(
                        message.id,
                        message.chatId,
                        message.author,
                        message.message,
                        message.updatedAt,
                        message.status,
                        message.errorMessage,
                        message.truncated
                    )
                }
            }
        }
    }

    override suspend fun getMessagesFromChat(chatId: Long): Flow<List<MessageDB>> {
        return appDatabase { db ->
            db.appDatabaseQueries.getMessagesFromChat(chatId).asFlow().map {
                it.executeAsList().map { query ->
                    MessageDB(
                        query.id,
                        query.chatId,
                        query.author,
                        query.message,
                        query.updatedAt,
                        query.status,
                        query.errorMessage,
                        query.truncated
                    )
                }
            }
        }
    }

    override suspend fun deleteMessagesFromChat(chatId: Long) {
        appDatabase { db ->
            db.appDatabaseQueries.deleteMessagesFromChat(chatId)
        }
    }

    override suspend fun deleteMessage(id: Long) {
        appDatabase { db ->
            db.appDatabaseQueries.deleteMessage(id)
        }
    }
}