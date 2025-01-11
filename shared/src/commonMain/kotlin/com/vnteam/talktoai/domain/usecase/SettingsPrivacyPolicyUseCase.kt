package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SettingsPrivacyPolicyUseCase {

    suspend fun getAppLanguage(): Flow<NetworkResult<String?>>

    suspend fun getPrivacyPolicy(appLang: String): Flow<NetworkResult<String>>
}