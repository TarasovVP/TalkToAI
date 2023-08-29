package com.vnstudio.talktoai.presentation.screens.settings.settings_theme

import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.usecases.SettingsThemeUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsThemeUseCaseImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): SettingsThemeUseCase {

    override suspend fun getAppTheme(): Flow<Int?> {
       return dataStoreRepository.getAppTheme()
    }

    override suspend fun setAppTheme(appTheme: Int) {
        dataStoreRepository.setAppTheme(appTheme)
    }
}