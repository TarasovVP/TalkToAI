package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface CreateUseCase {

    suspend fun getDemoObjectById(id: String): Flow<Chat?>

    suspend fun insertDemoObjectToDB(chat: Chat): Flow<Unit>

    suspend fun createDemoObject(chat: Chat): Flow<Unit>
}