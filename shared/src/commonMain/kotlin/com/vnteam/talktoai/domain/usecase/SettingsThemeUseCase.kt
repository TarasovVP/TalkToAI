package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SettingsThemeUseCase {

    suspend fun getAppTheme(): Flow<NetworkResult<Boolean?>>

    suspend fun setAppTheme(appTheme: Boolean)
}