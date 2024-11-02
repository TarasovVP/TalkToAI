package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ApiRepository {

    suspend fun getDemoObjectsFromApi(): Flow<List<Chat>?>

    suspend fun insertDemoObjectsToApi(chats: List<Chat>?): Flow<Unit>

    suspend fun getDemoObjectById(demoObjectId: String?): Flow<Chat?>

    suspend fun deleteDemoObjectById(demoObjectId: String): Flow<Unit>
}