package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Feedback
import kotlinx.coroutines.flow.Flow

interface SettingsListUseCase {

    fun currentUserEmail(): String

    fun insertFeedback(feedback: Feedback): Flow<Result<Unit>>
}