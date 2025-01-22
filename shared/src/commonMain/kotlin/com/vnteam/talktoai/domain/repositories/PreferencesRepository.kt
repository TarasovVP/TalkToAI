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
}