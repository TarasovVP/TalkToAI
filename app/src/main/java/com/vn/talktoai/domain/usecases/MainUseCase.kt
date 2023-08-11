package com.vn.talktoai.domain.usecases

import com.vn.talktoai.data.database.db_entities.Chat
import kotlinx.coroutines.flow.Flow

interface MainUseCase {

    suspend fun insertChat(chat: Chat)

    suspend fun getChats(): Flow<List<Chat>>
}