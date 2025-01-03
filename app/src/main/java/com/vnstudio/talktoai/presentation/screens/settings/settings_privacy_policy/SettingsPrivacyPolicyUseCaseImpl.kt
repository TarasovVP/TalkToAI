package com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy

import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsPrivacyPolicyUseCase
import kotlinx.coroutines.flow.Flow


class SettingsPrivacyPolicyUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
) : SettingsPrivacyPolicyUseCase {

    override suspend fun getAppLanguage(): Flow<String?> {
        return dataStoreRepository.getAppLang()
    }

    override suspend fun getPrivacyPolicy(appLang: String, result: (Result<String>) -> Unit) {
        return realDataBaseRepository.getPrivacyPolicy(appLang, result)
    }
}