package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SettingsLanguageUseCase {

    suspend fun getAppLanguage(): Flow<NetworkResult<String?>>

    suspend fun setAppLanguage(appLang: String)
}