package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.openai.request.ApiRequest
import kotlinx.coroutines.flow.Flow

interface AIRepository {
    fun sendRequest(
        apiRequest: ApiRequest,
        apiKey: String? = null,
    ): Flow<Result<AiTextResponse>>
}
