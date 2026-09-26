package com.vnteam.talktoai.data.network.ai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.request.Message
import com.vnteam.talktoai.domain.models.AiModel
import kotlinx.coroutines.flow.Flow

interface AiProvider {
    fun sendMessage(
        model: String,
        messages: List<Message>,
        apiKey: String? = null,
        temperature: Float? = null,
        maxOutputTokens: Int = AiModel.DEFAULT_MAX_OUTPUT_TOKENS,
    ): Flow<Result<AiTextResponse>>
}

data class AiTextResponse(
    val model: String,
    val content: String,
    val fallbackFrom: String? = null,
    val usage: TokenUsage? = null,
)

data class TokenUsage(
    val inputTokens: Int,
    val outputTokens: Int?,
    val cacheReadTokens: Int = 0,
    val cacheWriteTokens: Int = 0,
)
