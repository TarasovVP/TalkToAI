package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.network.ApiService
import com.vnteam.talktoai.data.network.handleResponse
import com.vnteam.talktoai.data.network.request.ApiRequest
import com.vnteam.talktoai.data.network.responses.ApiResponse
import com.vnteam.talktoai.domain.repositories.AIRepository
import kotlinx.coroutines.flow.flow

class AIRepositoryImpl(
    private val apiService: ApiService
) : AIRepository {

    override suspend fun sendRequest(apiRequest: ApiRequest) = flow {
        val httpResponse = apiService.sendRequest(apiRequest)
        emit(httpResponse.handleResponse<ApiResponse>())
    }
}