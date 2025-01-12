package com.vnteam.talktoai.domain.repositories

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    fun getIsDarkTheme(): Flow<Boolean>

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)

    fun getLanguage(): Flow<String?>

    suspend fun setLanguage(language: String)

    fun getIsBoardingSeen(): Flow<Boolean?>

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    fun getIsLoggedInUser(): Flow<Boolean?>

    suspend fun setLoggedInUser(isLoggedInUser: Boolean)

    fun getIsReviewVoted(): Flow<Boolean?>

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    suspend fun clearDataByKeys(keys: List<String>) {
        // TODO: Implement this method
    }
}