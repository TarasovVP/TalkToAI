package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow

interface SettingsPrivacyPolicyUseCase {

    suspend fun getAppLanguage(): Flow<Result<String?>>

    suspend fun getPrivacyPolicy(appLang: String): Flow<Result<String>>
}