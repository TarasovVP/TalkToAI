package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.SettingsListUseCase
import kotlinx.coroutines.flow.Flow


class SettingsFeedbackUseCaseImpl(
    private val realDataBaseRepository: RealDataBaseRepository,
    private val authRepository: AuthRepository,
) : SettingsListUseCase {

    override fun currentUserEmail() = authRepository.currentUserEmail()

    override fun insertFeedback(feedback: Feedback): Flow<NetworkResult<Unit>> =
        realDataBaseRepository.insertFeedback(feedback)
}