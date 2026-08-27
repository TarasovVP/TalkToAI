package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.openai.request.ApiRequest
import com.vnteam.talktoai.data.network.ai.openai.response.ApiResponse
import com.vnteam.talktoai.data.network.ai.openai.response.ModelsResponse
import kotlinx.coroutines.flow.Flow

interface AIRepository {

    fun sendRequest(
        apiRequest: ApiRequest,
        apiKey: String? = null,
    ): Flow<Result<ApiResponse>>

    fun getModels(apiKey: String? = null): Flow<Result<ModelsResponse>>
}
