package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.Feedback

interface SettingsListUseCase {

    fun currentUserEmail(): String

    fun insertFeedback(feedback: Feedback, result: (NetworkResult<Unit>) -> Unit)
}