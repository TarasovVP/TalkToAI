package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SettingsChatUseCase {

    suspend fun getChatSettings(): Flow<NetworkResult<Boolean?>>
}