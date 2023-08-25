package com.vnstudio.talktoai.presentation.settings.settings_privacy_policy

import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.SettingsPrivacyPolicyUseCase
import com.vnstudio.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsPrivacyPolicyUseCaseImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val realDataBaseRepository: RealDataBaseRepository
): SettingsPrivacyPolicyUseCase {

    override suspend fun getAppLanguage(): Flow<String?> {
        return dataStoreRepository.getAppLang()
    }

    override suspend fun getPrivacyPolicy(appLang: String, result: (Result<String>) -> Unit) {
        return realDataBaseRepository.getPrivacyPolicy(appLang, result)
    }
}