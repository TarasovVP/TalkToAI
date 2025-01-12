package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FeedbackUseCase(private val realDataBaseRepository: RealDataBaseRepository) {

    fun getFeedbacks(): Flow<Result<List<String>>> =
        realDataBaseRepository.getFeedbacks().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }

    fun insertFeedback(feedback: Feedback): Flow<Result<Unit>> =
        realDataBaseRepository.insertFeedback(feedback).map {
            Result.Success(Unit)
        }.catch {
            Result.Failure(it.message)
        }
}