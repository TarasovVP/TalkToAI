package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AppUseCase {

    suspend fun getIsDarkTheme(): Flow<NetworkResult<Boolean>>

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)

    suspend fun getLanguage(): Flow<NetworkResult<String?>>

    suspend fun setLanguage(language: String)

    suspend fun getIsBoardingSeen(): Flow<NetworkResult<Boolean?>>

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    suspend fun getIsLoggedInUser(): Flow<NetworkResult<Boolean?>>

    suspend fun getReviewVoted(): Flow<NetworkResult<Boolean?>>

    suspend fun setReviewVoted(isReviewVoted: Boolean)
}