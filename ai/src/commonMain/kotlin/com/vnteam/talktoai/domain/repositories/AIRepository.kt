package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.request.ApiRequest
import com.vnteam.talktoai.data.network.ai.responses.ApiResponse
import kotlinx.coroutines.flow.Flow

interface AIRepository {

    suspend fun sendRequest(apiRequest: ApiRequest): Flow<Result<ApiResponse>>
}