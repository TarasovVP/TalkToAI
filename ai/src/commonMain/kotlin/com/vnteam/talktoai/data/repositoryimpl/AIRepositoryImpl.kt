package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.network.UNKNOWN_ERROR
import com.vnteam.talktoai.data.network.ai.AIService
import com.vnteam.talktoai.data.network.ai.request.ApiRequest
import com.vnteam.talktoai.data.network.ai.responses.ApiResponse
import com.vnteam.talktoai.data.network.ai.responses.ModelsResponse
import com.vnteam.talktoai.data.network.handleResponse
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AIRepository
import kotlinx.coroutines.flow.flow

class AIRepositoryImpl(
    private val aIService: AIService,
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
