package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.APP_LANGUAGE
import com.vnteam.talktoai.data.ID_TOKEN
import com.vnteam.talktoai.data.IS_DARK_THEME
import com.vnteam.talktoai.data.IS_ONBOARDING_SEEN
import com.vnteam.talktoai.data.IS_REVIEW_VOTE
import com.vnteam.talktoai.data.USER_EMAIL
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

    override fun getUserEmail(): Flow<String?> {
        return preferencesFactory.getString(USER_EMAIL)
    }

    override suspend fun setUserEmail(userEmail: String) {
        preferencesFactory.putString(USER_EMAIL, userEmail)
    }

    override fun getIdToken(): Flow<String?> {
        return preferencesFactory.getString(ID_TOKEN)
    }

    override suspend fun setIdToken(idToken: String) {
        preferencesFactory.putString(ID_TOKEN, idToken)
    }

    override fun getIsReviewVoted(): Flow<Boolean?> {
        return preferencesFactory.getBoolean(IS_REVIEW_VOTE)
    }

    override suspend fun setReviewVoted(isReviewVoted: Boolean) {
        preferencesFactory.putBoolean(IS_REVIEW_VOTE, isReviewVoted)
    }
}