package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ReviewUseCase(private val preferencesRepository: PreferencesRepository) {

    fun getIsReviewVoted(): Flow<Result<Boolean?>> {
        return preferencesRepository.getIsReviewVoted().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }

    suspend fun setReviewVoted(isReviewVoted: Boolean) {
        preferencesRepository.setReviewVoted(isReviewVoted)
    }
}