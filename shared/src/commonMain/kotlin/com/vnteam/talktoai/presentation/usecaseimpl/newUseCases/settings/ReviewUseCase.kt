package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.DataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ReviewUseCase(private val preferencesRepository: PreferencesRepository):
    DataUseCase<Boolean, Flow<Result<Boolean?>>> {

    override fun get(): Flow<Result<Boolean?>> {
        return preferencesRepository.getIsReviewVoted().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }

    override suspend fun set(params: Boolean) {
        preferencesRepository.setReviewVoted(params)
    }
}