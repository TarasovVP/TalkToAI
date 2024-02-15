package com.vnstudio.talktoai.data.database.dao

import com.vnstudio.talktoai.AppDatabaseQueries
import com.vnstudio.talktoai.data.database.db_entities.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ChatDaoImpl(private val appDatabaseQueries: AppDatabaseQueries): ChatDao {

        override fun insertChats(chats: List<Chat>) {
            appDatabaseQueries.transaction {
                chats.forEach { chat ->
                    appDatabaseQueries.insertChat(
                        chat.id,
                        chat.name,
                        chat.updated,
                        chat.listOrder.toLong()
                    )
                }
            }
        }

        override fun insertChat(chat: Chat) {
            appDatabaseQueries.insertChat(
                chat.id,
                chat.name,
                chat.updated,
                chat.listOrder.toLong()
            )
        }

        override fun deleteChats(chatIds: List<Long>) {
            appDatabaseQueries.transaction {
                chatIds.forEach { chatId ->
                    appDatabaseQueries.deleteChat(chatId)
                }
            }
        }

        override fun getChats(): Flow<List<Chat>> {
            val chats = appDatabaseQueries.getChats().executeAsList().map { chat ->
                Chat(
                    chat.id,
                    chat.name,
                    chat.updated,
                    chat.listOrder.toInt()
                ) }

            return flowOf( chats )
        }

        override fun getLastUpdatedChat(): Flow<Chat?> {
            val chat = appDatabaseQueries.getLastUpdatedChat().executeAsOneOrNull()
            return flowOf( chat?.let {
                Chat(
                    it.id,
                    it.name,
                    it.updated,
                    it.listOrder.toInt()
                ) })
        }

        override fun getChatById(chatId: Long): Flow<Chat?> {
            val chat = appDatabaseQueries.getChatById(chatId).executeAsOneOrNull()
            return flowOf( chat?.let {
                Chat(
                    it.id,
                    it.name,
                    it.updated,
                    it.listOrder.toInt()
                ) })
        }

        override fun updateChat(chat: Chat) {
            appDatabaseQueries.updateChat(
                chat.name,
                chat.updated,
                chat.listOrder.toLong(),
                chat.id
            )
        }

        override fun updateChats(chats: List<Chat>) {
            appDatabaseQueries.transaction {
                chats.forEach { chat ->
                    appDatabaseQueries.updateChat(
                        chat.name,
                        chat.updated,
                        chat.listOrder.toLong(),
                        chat.id
                    )
                }
            }
        }

        override fun deleteChat(chat: Chat) {
            appDatabaseQueries.deleteChat(chat.id)
        }
}