package com.vnteam.talktoai.domain.usecase

import kotlinx.coroutines.flow.Flow

interface SettingsThemeUseCase {

    suspend fun getAppTheme(): Flow<Boolean?>

    suspend fun setAppTheme(appTheme: Boolean)
}