package com.vnstudio.talktoai.domain.repositories

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    suspend fun onBoardingSeen(): Flow<Boolean?>

    suspend fun setAppLang(appLang: String)

    suspend fun getAppLang(): Flow<String?>

    suspend fun setAppTheme(appTheme: Int)

    suspend fun getAppTheme(): Flow<Int?>

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    suspend fun reviewVoted(): Flow<Boolean?>

    suspend fun clearDataByKeys(keys: List<Preferences.Key<*>>)
}