package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun getIsDarkTheme(): Flow<NetworkResult<Boolean>>

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)

    suspend fun getLanguage(): Flow<NetworkResult<String?>>

    suspend fun setLanguage(language: String)

    suspend fun getIsBoardingSeen(): Flow<NetworkResult<Boolean?>>

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    suspend fun getIsLoggedInUser(): Flow<NetworkResult<Boolean?>>

    suspend fun setLoggedInUser(isLoggedInUser: Boolean)

    suspend fun getIsReviewVoted(): Flow<NetworkResult<Boolean?>>

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    suspend fun clearDataByKeys(keys: List<String>) {
        // TODO: Implement this method
    }
}