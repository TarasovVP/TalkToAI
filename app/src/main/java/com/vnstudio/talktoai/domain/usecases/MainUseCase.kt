package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.data.database.db_entities.Chat
import kotlinx.coroutines.flow.Flow

interface MainUseCase {

    suspend fun insertChat(chat: Chat)

    suspend fun getChats(): Flow<List<Chat>>

    suspend fun updateChat(chat: Chat)

    suspend fun deleteChat(chat: Chat)
}