package com.vnteam.talktoai.data.database.dao

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.vnteam.talktoai.ChatDB
import com.vnteam.talktoai.data.database.SharedDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatDaoImpl(private val appDatabase: SharedDatabase) : ChatDao {
    override suspend fun clearChats() {
        appDatabase { db ->
            db.appDatabaseQueries.clearChats()
        }
    }

    override suspend fun insertChats(chats: List<ChatDB>) {
        appDatabase { db ->
            db.appDatabaseQueries.transaction {
                chats.forEach { chat ->
                    db.appDatabaseQueries.insertChats(
                        id = chat.id,
                        name = chat.name,
                        updated = chat.updated,
                        listOrder = chat.listOrder
                    )
                }
            }
        }
    }

    override suspend fun getChats(): Flow<List<ChatDB>> = callbackFlow {
        appDatabase { db ->
            trySend(db.appDatabaseQueries.getChats().awaitAsList()).isSuccess
        }
        awaitClose { }
    }

    override suspend fun deleteChatById(id: Long) {
        appDatabase { db ->
            db.appDatabaseQueries.deleteChatById(id)
        }
    }

    override suspend fun insertChat(chat: ChatDB) {
        appDatabase { db ->
            db.appDatabaseQueries.insertChat(
                chat.id,
                chat.name,
                chat.updated,
                chat.listOrder
            )
        }
    }

    override suspend fun deleteChats(chatIds: List<Long>) {
        appDatabase { db ->
            db.appDatabaseQueries.transaction {
                chatIds.forEach { chatId ->
                    db.appDatabaseQueries.deleteChats(chatId)
                }
            }
        }
    }

    override suspend fun getLastUpdatedChat(): Flow<ChatDB?> = callbackFlow {
        appDatabase { db ->
            trySend(db.appDatabaseQueries.getLastUpdatedChat().awaitAsOneOrNull()).isSuccess
            awaitClose { }
        }
    }

    override suspend fun getChatById(chatId: Long): Flow<ChatDB?> = callbackFlow {
        appDatabase { db ->
            trySend(db.appDatabaseQueries.getChatById(chatId).awaitAsOneOrNull()).isSuccess
            awaitClose { }
        }
    }

    override suspend fun updateChat(chat: ChatDB) {
        appDatabase { db ->
            db.appDatabaseQueries.updateChat(
                chat.name,
                chat.updated,
                chat.listOrder,
                chat.id
            )
        }
    }

    override suspend fun updateChats(chats: List<ChatDB>) {
        appDatabase { db ->
            db.appDatabaseQueries.transaction {
                chats.forEach { chat ->
                    db.appDatabaseQueries.updateChat(
                        chat.name,
                        chat.updated,
                        chat.listOrder,
                        chat.id
                    )
                }
            }
        }
    }

    override suspend fun deleteChat(chat: ChatDB) {
        appDatabase { db ->
            db.appDatabaseQueries.deleteChatById(chat.id)
        }
    }
}