package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.APP_LANGUAGE
import com.vnteam.talktoai.data.IS_DARK_THEME
import com.vnteam.talktoai.data.USER_LOGIN
import com.vnteam.talktoai.data.IS_ONBOARDING_SEEN
import com.vnteam.talktoai.data.IS_REVIEW_VOTE
import com.vnteam.talktoai.data.local.PreferencesFactory
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(private val preferencesFactory: PreferencesFactory) :
    PreferencesRepository {
    override fun getIsDarkTheme(): Flow<Boolean> {
        return preferencesFactory.getBoolean(IS_DARK_THEME)
    }

    override suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        preferencesFactory.putBoolean(IS_DARK_THEME, isDarkTheme)
    }

    override fun getLanguage(): Flow<String?> {
        return preferencesFactory.getString(APP_LANGUAGE)
    }

    override suspend fun setLanguage(language: String) {
        preferencesFactory.putString(APP_LANGUAGE, language)
    }

    override fun getIsBoardingSeen(): Flow<Boolean?> {
        return preferencesFactory.getBoolean(IS_ONBOARDING_SEEN)
    }

    override suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean) {
        preferencesFactory.putBoolean(IS_ONBOARDING_SEEN, isOnBoardingSeen)
        println("appTAG PreferencesRepositoryImpl setOnBoardingSeen $isOnBoardingSeen")
    }

    override fun getUserLogin(): Flow<String?> {
        return preferencesFactory.getString(USER_LOGIN)
    }

    override suspend fun setUserLogin(userLogin: String) {
        preferencesFactory.putString(USER_LOGIN, userLogin)
    }

    override fun getIsReviewVoted(): Flow<Boolean?> {
        return preferencesFactory.getBoolean(IS_REVIEW_VOTE)
    }

    override suspend fun setReviewVoted(isReviewVoted: Boolean) {
        preferencesFactory.putBoolean(IS_REVIEW_VOTE, isReviewVoted)
    }
}