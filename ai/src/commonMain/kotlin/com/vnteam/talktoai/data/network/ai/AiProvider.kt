package com.vnteam.talktoai.data.network.ai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.request.MessageApi
import kotlinx.coroutines.flow.Flow

interface AiProvider {
    fun sendMessage(
        model: String,
        temperature: Float,
        messages: List<MessageApi>,
        apiKey: String? = null,
    ): Flow<Result<AiTextResponse>>
}

data class AiTextResponse(val model: String, val content: String)
