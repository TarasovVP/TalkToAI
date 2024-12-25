package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.APP_LANGUAGE
import com.vnteam.talktoai.data.IS_DARK_THEME
import com.vnteam.talktoai.data.IS_LOGGED_IN_USER
import com.vnteam.talktoai.data.IS_ONBOARDING_SEEN
import com.vnteam.talktoai.data.IS_REVIEW_VOTE
import com.vnteam.talktoai.data.local.PreferencesFactory
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(private val preferencesFactory: PreferencesFactory) :
    PreferencesRepository {
    override suspend fun getIsDarkTheme(): Flow<Boolean> {
        return preferencesFactory.getBoolean(IS_DARK_THEME)
    }

    override suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        preferencesFactory.putBoolean(IS_DARK_THEME, isDarkTheme)
    }

    override suspend fun getLanguage(): Flow<String?> {
        return preferencesFactory.getString(APP_LANGUAGE)
    }

    override suspend fun setLanguage(language: String) {
        preferencesFactory.putString(APP_LANGUAGE, language)
    }

    override suspend fun getIsBoardingSeen(): Flow<Boolean?> {
        return preferencesFactory.getBoolean(IS_ONBOARDING_SEEN)
    }

    override suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean) {
        preferencesFactory.putBoolean(IS_ONBOARDING_SEEN, isOnBoardingSeen)
    }

    override suspend fun getIsLoggedInUser(): Flow<Boolean?> {
        return preferencesFactory.getBoolean(IS_LOGGED_IN_USER)
    }

    override suspend fun setLoggedInUser(isLoggedInUser: Boolean) {
        preferencesFactory.putBoolean(IS_LOGGED_IN_USER, isLoggedInUser)
    }

    override suspend fun getIsReviewVoted(): Flow<Boolean?> {
        return preferencesFactory.getBoolean(IS_REVIEW_VOTE)
    }

    override suspend fun setReviewVoted(isReviewVoted: Boolean) {
        preferencesFactory.putBoolean(IS_REVIEW_VOTE, isReviewVoted)
    }
}