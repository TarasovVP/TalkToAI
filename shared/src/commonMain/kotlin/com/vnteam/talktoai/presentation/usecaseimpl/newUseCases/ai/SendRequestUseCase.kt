package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.ai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.openai.request.ApiRequest
import com.vnteam.talktoai.domain.repositories.AIRepository
import kotlinx.coroutines.flow.Flow

class SendRequestUseCase(private val aiRepository: AIRepository) {

    fun execute(apiRequest: ApiRequest, apiKey: String? = null): Flow<Result<AiTextResponse>> {
        return aiRepository.sendRequest(apiRequest, apiKey)
    }
}
