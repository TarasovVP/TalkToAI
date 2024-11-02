package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface DetailsUseCase {

    suspend fun getDemoObjectById(id: String): Flow<Chat?>
}