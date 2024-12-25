package com.vnteam.talktoai.domain.repositories

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun getIsDarkTheme(): Flow<Boolean>

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)

    suspend fun getLanguage(): Flow<String?>

    suspend fun setLanguage(language: String)

    suspend fun getIsBoardingSeen(): Flow<Boolean?>

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    suspend fun getIsLoggedInUser(): Flow<Boolean?>

    suspend fun setLoggedInUser(isLoggedInUser: Boolean)

    suspend fun getIsReviewVoted(): Flow<Boolean?>

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    suspend fun clearDataByKeys(keys: List<String>) {
        // TODO: Implement this method
    }
}