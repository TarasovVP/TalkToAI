package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.AppUseCase
import kotlinx.coroutines.flow.Flow

class AppUseCaseImpl(private val preferencesRepository: PreferencesRepository) :
    AppUseCase {
    override suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        preferencesRepository.setIsDarkTheme(isDarkTheme)
    }

    override suspend fun getIsDarkTheme(): Flow<NetworkResult<Boolean>> {
        return preferencesRepository.getIsDarkTheme()
    }

    override suspend fun setLanguage(language: String) {
        preferencesRepository.setLanguage(language)
    }

    override suspend fun getLanguage(): Flow<NetworkResult<String?>> {
        return preferencesRepository.getLanguage()
    }

    override suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean) {
        preferencesRepository.setOnBoardingSeen(isOnBoardingSeen)
    }

    override suspend fun getIsLoggedInUser(): Flow<NetworkResult<Boolean?>> {
        return preferencesRepository.getIsLoggedInUser()
    }

    override suspend fun getIsBoardingSeen(): Flow<NetworkResult<Boolean?>> {
        return preferencesRepository.getIsBoardingSeen()
    }

    override suspend fun setReviewVoted(isReviewVoted: Boolean) {
        preferencesRepository.setReviewVoted(isReviewVoted)
    }

    override suspend fun getReviewVoted(): Flow<NetworkResult<Boolean?>> {
        return preferencesRepository.getIsReviewVoted()
    }
}