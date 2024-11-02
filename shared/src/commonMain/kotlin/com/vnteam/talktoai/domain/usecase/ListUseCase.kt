package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ListUseCase {

    suspend fun clearChats()

    suspend fun getDemoObjectsFromApi(): Flow<List<Chat>?>

    suspend fun insertDemoObjectsToDB(chats: List<Chat>): Flow<Unit>

    suspend fun getDemoObjectsFromDB(): Flow<List<Chat>>

    suspend fun deleteDemoObjectById(id: String): Flow<Unit>
}