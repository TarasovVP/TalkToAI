package com.vnteam.talktoai.domain.usecase

import kotlinx.coroutines.flow.Flow

interface SettingsLanguageUseCase {

    suspend fun getAppLanguage(): Flow<String?>

    suspend fun setAppLanguage(appLang: String)
}