package com.vnstudio.talktoai.domain.usecases

import kotlinx.coroutines.flow.Flow

interface SettingsThemeUseCase {

    suspend fun getAppTheme(): Flow<Int?>

    suspend fun setAppTheme(appTheme: Int)
}