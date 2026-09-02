package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.ai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.request.Message
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.repositories.AIRepository
import kotlinx.coroutines.flow.Flow

class SendRequestUseCase(private val aiRepository: AIRepository) {

    fun execute(
        model: String,
        messages: List<Message>,
        apiKey: String? = null,
        providerType: AiProviderType = AiProviderType.OPENAI,
        temperature: Float? = null,
    ): Flow<Result<AiTextResponse>> {
        return aiRepository.sendRequest(model, messages, apiKey, providerType, temperature)
    }
}
