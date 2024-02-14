package com.vnstudio.talktoai.domain.usecases

import kotlinx.coroutines.flow.Flow

interface SettingsChatUseCase {

    suspend fun getChatSettings(): Flow<Boolean?>
}