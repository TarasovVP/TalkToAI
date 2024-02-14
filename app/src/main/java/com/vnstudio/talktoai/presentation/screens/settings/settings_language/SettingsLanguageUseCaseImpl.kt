package com.vnstudio.talktoai.presentation.screens.settings.settings_language

import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.usecases.SettingsLanguageUseCase
import kotlinx.coroutines.flow.Flow


class SettingsLanguageUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
) : SettingsLanguageUseCase {

    override suspend fun getAppLanguage(): Flow<String?> {
        return dataStoreRepository.getAppLang()
    }

    override suspend fun setAppLanguage(appLang: String) {
        dataStoreRepository.setAppLang(appLang)
    }
}