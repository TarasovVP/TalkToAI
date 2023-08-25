package com.vnstudio.talktoai.domain.usecases

import kotlinx.coroutines.flow.Flow

interface SettingsBlockerUseCase {

    suspend fun getBlockerTurnOn(): Flow<Boolean?>
}