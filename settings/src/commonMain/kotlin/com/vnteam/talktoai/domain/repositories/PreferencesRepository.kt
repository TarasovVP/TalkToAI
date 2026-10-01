package com.vnteam.talktoai.domain.repositories

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    fun getIsDarkTheme(): Flow<Boolean>

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)

    fun getLanguage(): Flow<String?>

    suspend fun setLanguage(language: String)

    fun getIsBoardingSeen(): Flow<Boolean?>

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean)

    fun getUserEmail(): Flow<String?>

    suspend fun setUserEmail(userEmail: String)

    fun getIdToken(): Flow<String?>

    suspend fun setIdToken(idToken: String)

    fun getIsReviewVoted(): Flow<Boolean?>

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    fun getAiTier(): Flow<String?>

    suspend fun setAiTier(tier: String)

    fun getAiProvider(): Flow<String?>

    suspend fun setAiProvider(provider: String)

    fun getAiModelsCache(): Flow<String?>

    suspend fun setAiModelsCache(json: String)

    fun getAiModelsFetchedAt(): Flow<String?>

    suspend fun setAiModelsFetchedAt(timestamp: String)

    fun getGlobalSystemContext(): Flow<String?>

    suspend fun setGlobalSystemContext(context: String)

    fun getUid(): Flow<String?>

    suspend fun setUid(uid: String)

    fun getRefreshToken(): Flow<String?>

    suspend fun setRefreshToken(refreshToken: String)
}
