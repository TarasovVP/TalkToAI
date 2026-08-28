package com.vnteam.talktoai.data.network.ai.anthropic.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnthropicRequest(
    val model: String,
    @SerialName("max_tokens") val maxTokens: Int = DEFAULT_MAX_TOKENS,
    val temperature: Float,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val system: String? = null,
    val messages: List<AnthropicMessage>,
    val stream: Boolean = true,
) {
    companion object {
        const val DEFAULT_MAX_TOKENS = 8192
    }
}

@Serializable
data class AnthropicMessage(
    val role: String,
    val content: List<AnthropicContentBlock>,
)

@Serializable
data class AnthropicContentBlock(
    val type: String = "text",
    val text: String,
)
