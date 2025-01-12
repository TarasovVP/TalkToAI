package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow

interface AppUseCase {

    suspend fun getIsDarkTheme(): Flow<Result<Boolean>>

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)

    suspend fun getLanguage(): Flow<Result<String?>>

    suspend fun setLanguage(language: String)

    suspend fun getIsBoardingSeen(): Flow<Result<Boolean?>>

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    suspend fun getIsLoggedInUser(): Flow<Result<Boolean?>>

    suspend fun getReviewVoted(): Flow<Result<Boolean?>>

    suspend fun setReviewVoted(isReviewVoted: Boolean)
}