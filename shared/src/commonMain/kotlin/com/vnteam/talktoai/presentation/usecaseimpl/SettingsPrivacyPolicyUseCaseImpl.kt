package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.SettingsPrivacyPolicyUseCase
import kotlinx.coroutines.flow.Flow

class SettingsPrivacyPolicyUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
) : SettingsPrivacyPolicyUseCase {

    override suspend fun getAppLanguage(): Flow<String?> {
        return preferencesRepository.getLanguage()
    }

    override suspend fun getPrivacyPolicy(appLang: String, result: (NetworkResult<String>) -> Unit) {
        return realDataBaseRepository.getPrivacyPolicy(appLang, result)
    }
}