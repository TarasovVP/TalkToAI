package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.data.network.Result
import com.vnstudio.talktoai.domain.models.Feedback
import kotlinx.coroutines.flow.Flow

interface SettingsListUseCase {

    suspend fun getAppLanguage(): Flow<String?>

    fun insertFeedback(feedback: Feedback, result: (Result<Unit>) -> Unit)
}