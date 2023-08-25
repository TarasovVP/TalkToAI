package com.vnstudio.talktoai.domain.usecases

import kotlinx.coroutines.flow.Flow

interface SettingsLanguageUseCase {

    suspend fun getAppLanguage(): Flow<String?>

    suspend fun setAppLanguage(appLang: String)
}