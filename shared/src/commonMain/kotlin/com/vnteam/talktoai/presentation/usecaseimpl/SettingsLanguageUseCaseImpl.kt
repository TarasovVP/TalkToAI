package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.SettingsLanguageUseCase
import kotlinx.coroutines.flow.Flow

class SettingsLanguageUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
) : SettingsLanguageUseCase {

    override suspend fun getAppLanguage(): Flow<NetworkResult<String?>> {
        return preferencesRepository.getLanguage()
    }

    override suspend fun setAppLanguage(appLang: String) {
        preferencesRepository.setLanguage(appLang)
    }
}