package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.ai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.responses.ModelsResponse
import com.vnteam.talktoai.domain.repositories.AIRepository
import kotlinx.coroutines.flow.Flow

class GetModelsUseCase(private val aiRepository: AIRepository) {

    suspend fun execute(apiKey: String? = null): Flow<Result<ModelsResponse>> {
        return aiRepository.getModels(apiKey)
    }
}
