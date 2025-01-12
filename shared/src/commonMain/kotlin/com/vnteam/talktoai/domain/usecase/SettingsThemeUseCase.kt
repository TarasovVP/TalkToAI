package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow

interface SettingsThemeUseCase {

    suspend fun getAppTheme(): Flow<Result<Boolean?>>

    suspend fun setAppTheme(appTheme: Boolean)
}