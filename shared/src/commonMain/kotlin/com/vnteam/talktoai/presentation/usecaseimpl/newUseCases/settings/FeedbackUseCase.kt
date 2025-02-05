package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.DataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FeedbackUseCase(private val realDataBaseRepository: RealDataBaseRepository) :
    DataUseCase<Feedback, Flow<Result<List<Feedback>>>> {

    override fun get(): Flow<Result<List<Feedback>>> {
        return realDataBaseRepository.getFeedbacks().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }

    override suspend fun set(params: Feedback) {
        realDataBaseRepository.insertFeedback(params)
    }
}