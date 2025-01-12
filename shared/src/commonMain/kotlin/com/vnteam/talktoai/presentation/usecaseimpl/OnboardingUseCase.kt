package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class OnboardingUseCase(private val preferencesRepository: PreferencesRepository) {

    fun getIsBoardingSeen(): Flow<Result<Boolean?>> {
        return preferencesRepository.getIsBoardingSeen().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }

    suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean) {
        preferencesRepository.setOnBoardingSeen(isOnBoardingSeen)
    }
}