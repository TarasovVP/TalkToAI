package com.vnstudio.talktoai.presentation.screens.settings.settings_feedback

import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.repositories.AuthRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsListUseCase
import javax.inject.Inject

class SettingsFeedbackUseCaseImpl @Inject constructor(
    private val realDataBaseRepository: RealDataBaseRepository,
    private val authRepository: AuthRepository,
) : SettingsListUseCase {

    override fun currentUserEmail() = authRepository.currentUserEmail()

    override fun insertFeedback(feedback: Feedback, result: (Result<Unit>) -> Unit) =
        realDataBaseRepository.insertFeedback(feedback) {
            result.invoke(it)
        }
}