package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.network.UNKNOWN_ERROR
import com.vnteam.talktoai.data.network.ai.openai.OpenAiService
import com.vnteam.talktoai.data.network.ai.openai.request.ApiRequest
import com.vnteam.talktoai.data.network.ai.openai.response.ApiResponse
import com.vnteam.talktoai.data.network.ai.openai.response.ModelsResponse
import com.vnteam.talktoai.data.network.handleResponse
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AIRepository
import kotlinx.coroutines.flow.flow

class AIRepositoryImpl(
    private val aIService: OpenAiService,
) : AIRepository {

    override fun sendRequest(apiRequest: ApiRequest, apiKey: String?) = flow {
        try {
            emit(aIService.sendRequest(apiRequest, apiKey).handleResponse<ApiResponse>())
        } catch (e: Exception) {
            emit(Result.Failure(e.message ?: UNKNOWN_ERROR))
        }
    }

    override fun getModels(apiKey: String?) = flow {
        try {
            emit(aIService.getModels(apiKey).handleResponse<ModelsResponse>())
        } catch (e: Exception) {
            emit(Result.Failure(e.message ?: UNKNOWN_ERROR))
        }
    }
}
