package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow

interface SettingsLanguageUseCase {

    suspend fun getAppLanguage(): Flow<Result<String?>>

    suspend fun setAppLanguage(appLang: String)
}