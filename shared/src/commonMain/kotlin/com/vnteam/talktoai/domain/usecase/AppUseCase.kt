package com.vnteam.talktoai.domain.usecase

import kotlinx.coroutines.flow.Flow

interface AppUseCase {

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)

    suspend fun getIsDarkTheme(): Flow<Boolean>

    suspend fun setLanguage(language: String)

    suspend fun getLanguage(): Flow<String?>

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    suspend fun getIsBoardingSeen(): Flow<Boolean?>

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    suspend fun reviewVoted(): Flow<Boolean?>
}