package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.network.ai.AIService
import com.vnteam.talktoai.data.network.ai.request.ApiRequest
import com.vnteam.talktoai.data.network.ai.responses.ApiResponse
import com.vnteam.talktoai.data.network.handleResponse
import com.vnteam.talktoai.domain.repositories.AIRepository
import kotlinx.coroutines.flow.flow

class AIRepositoryImpl(
    private val aIService: AIService
) : AIRepository {

    override suspend fun sendRequest(apiRequest: ApiRequest) = flow {
        val httpResponse = aIService.sendRequest(apiRequest)
        emit(httpResponse.handleResponse<ApiResponse>())
    }
}