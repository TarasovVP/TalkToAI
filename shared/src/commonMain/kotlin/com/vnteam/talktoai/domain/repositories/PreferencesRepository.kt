package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun getIsDarkTheme(): Flow<Result<Boolean>>

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)

    suspend fun getLanguage(): Flow<Result<String?>>

    suspend fun setLanguage(language: String)

    suspend fun getIsBoardingSeen(): Flow<Result<Boolean?>>

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    suspend fun getIsLoggedInUser(): Flow<Result<Boolean?>>

    suspend fun setLoggedInUser(isLoggedInUser: Boolean)

    suspend fun getIsReviewVoted(): Flow<Result<Boolean?>>

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    suspend fun clearDataByKeys(keys: List<String>) {
        // TODO: Implement this method
    }
}