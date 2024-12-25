package com.vnteam.talktoai.domain.usecase

import kotlinx.coroutines.flow.Flow

interface AppUseCase {

    suspend fun getIsDarkTheme(): Flow<Boolean>

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)

    suspend fun getLanguage(): Flow<String?>

    suspend fun setLanguage(language: String)

    suspend fun getIsBoardingSeen(): Flow<Boolean?>

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    suspend fun getIsLoggedInUser(): Flow<Boolean?>

    suspend fun getReviewVoted(): Flow<Boolean?>

    suspend fun setReviewVoted(isReviewVoted: Boolean)
}