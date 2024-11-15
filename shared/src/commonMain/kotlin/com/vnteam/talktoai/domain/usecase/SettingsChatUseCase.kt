package com.vnteam.talktoai.domain.usecase

import kotlinx.coroutines.flow.Flow

interface SettingsChatUseCase {

    suspend fun getChatSettings(): Flow<Boolean?>
}