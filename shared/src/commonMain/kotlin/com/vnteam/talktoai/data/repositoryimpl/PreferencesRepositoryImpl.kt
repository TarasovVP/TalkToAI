package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.IS_DARK_THEME
import com.vnteam.talktoai.data.APP_LANGUAGE
import com.vnteam.talktoai.data.local.PreferencesFactory
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(private val preferencesFactory: PreferencesFactory) :
    PreferencesRepository {
    override suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        preferencesFactory.putBoolean(IS_DARK_THEME, isDarkTheme)
    }

    override suspend fun getIsDarkTheme(): Flow<Boolean> {
        return preferencesFactory.getBoolean(IS_DARK_THEME)
    }

    override suspend fun setLanguage(language: String) {
        preferencesFactory.putString(APP_LANGUAGE, language)
    }

    override suspend fun getLanguage(): Flow<String?> {
        return preferencesFactory.getString(APP_LANGUAGE)
    }


}