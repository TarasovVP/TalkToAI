package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.sealed_classes.Result

interface SettingsListUseCase {

    fun currentUserEmail(): String

    fun insertFeedback(feedback: Feedback, result: (Result<Unit>) -> Unit)
}