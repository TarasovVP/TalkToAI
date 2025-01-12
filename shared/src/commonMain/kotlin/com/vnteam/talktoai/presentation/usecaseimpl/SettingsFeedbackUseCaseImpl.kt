package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.SettingsListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsFeedbackUseCaseImpl(
    private val realDataBaseRepository: RealDataBaseRepository,
    private val authRepository: AuthRepository,
) : SettingsListUseCase {

    override fun currentUserEmail() = authRepository.currentUserEmail()

    override fun insertFeedback(feedback: Feedback): Flow<Result<Unit>> =
        realDataBaseRepository.insertFeedback(feedback).map {
            Result.Success(Unit)
        }.catch {
            Result.Failure(it.message)
        }
}