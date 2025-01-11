package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.SettingsThemeUseCase
import kotlinx.coroutines.flow.Flow


class SettingsThemeUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
) : SettingsThemeUseCase {

    override suspend fun getAppTheme(): Flow<NetworkResult<Boolean?>> {
        return preferencesRepository.getIsDarkTheme()
    }

    override suspend fun setAppTheme(appTheme: Boolean) {
        preferencesRepository.setIsDarkTheme(appTheme)
    }
}