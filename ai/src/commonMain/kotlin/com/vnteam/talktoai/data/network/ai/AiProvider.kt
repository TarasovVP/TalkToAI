package com.vnteam.talktoai.data.network.ai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.request.Message
import kotlinx.coroutines.flow.Flow

interface AiProvider {
    fun sendMessage(
        model: String,
        temperature: Float,
        messages: List<Message>,
        apiKey: String? = null,
    ): Flow<Result<AiTextResponse>>
}

data class AiTextResponse(
    val model: String,
    val content: String,
    val fallbackFrom: String? = null,
)
