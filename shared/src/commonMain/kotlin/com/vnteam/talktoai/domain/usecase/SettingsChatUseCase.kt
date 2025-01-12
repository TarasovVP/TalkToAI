package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow

interface SettingsChatUseCase {

    suspend fun getChatSettings(): Flow<Result<Boolean?>>
}