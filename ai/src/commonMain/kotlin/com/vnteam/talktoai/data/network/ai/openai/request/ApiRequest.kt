package com.vnteam.talktoai.data.network.ai.openai.request

import com.vnteam.talktoai.domain.models.AiModel
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest(
    val model: String,
    val messages: List<OpenAiMessage>,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val temperature: Float? = null,
    @EncodeDefault @SerialName("max_completion_tokens") val maxCompletionTokens: Int = AiModel.DEFAULT_MAX_OUTPUT_TOKENS,
    @EncodeDefault val stream: Boolean = true,
    @EncodeDefault @SerialName("stream_options") val streamOptions: StreamOptions = StreamOptions(),
)

@Serializable
data class StreamOptions(
    @EncodeDefault @SerialName("include_usage") val includeUsage: Boolean = true,
)
