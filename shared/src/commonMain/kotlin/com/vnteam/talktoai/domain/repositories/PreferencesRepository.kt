package com.vnteam.talktoai.domain.repositories

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)

    suspend fun getIsDarkTheme(): Flow<Boolean>

    suspend fun setLanguage(language: String)

    suspend fun getLanguage(): Flow<String?>

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    suspend fun getIsBoardingSeen(): Flow<Boolean?>

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    suspend fun getIsReviewVoted(): Flow<Boolean?>
}