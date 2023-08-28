package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.domain.sealed_classes.Result
import kotlinx.coroutines.flow.Flow

interface SettingsPrivacyPolicyUseCase {

    suspend fun getAppLanguage(): Flow<String?>

    suspend fun getPrivacyPolicy(appLang: String, result: (Result<String>) -> Unit)
}