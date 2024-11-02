package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.AppUseCase
import kotlinx.coroutines.flow.Flow

class AppUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    AppUseCase {
    override suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        preferencesRepository.setIsDarkTheme(isDarkTheme)
    }

    override suspend fun getIsDarkTheme(): Flow<Boolean> {
        return preferencesRepository.getIsDarkTheme()
    }

    override suspend fun setLanguage(language: String) {
        preferencesRepository.setLanguage(language)
    }

    override suspend fun getLanguage(): Flow<String?> {
        return preferencesRepository.getLanguage()
    }

}