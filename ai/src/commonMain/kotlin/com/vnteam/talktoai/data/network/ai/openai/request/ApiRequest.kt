package com.vnteam.talktoai.data.network.ai.openai.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest(
    val model: String,
    val messages: List<MessageApi>,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val temperature: Float? = null,
    @EncodeDefault @SerialName("max_completion_tokens") val maxCompletionTokens: Int = DEFAULT_MAX_COMPLETION_TOKENS,
) {
    companion object {
        const val DEFAULT_MAX_COMPLETION_TOKENS = 8192
    }
}