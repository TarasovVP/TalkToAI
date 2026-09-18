package com.vnteam.talktoai.data.network.ai.anthropic.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
data class AnthropicRequest(
    val model: String,
    @EncodeDefault @SerialName("max_tokens") val maxTokens: Int = DEFAULT_MAX_TOKENS,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val system: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val temperature: Float? = null,
    val messages: List<AnthropicMessage>,
    @EncodeDefault val stream: Boolean = true,
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

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class AnthropicContentBlock

@Serializable
@SerialName("text")
data class AnthropicTextBlock(
    val text: String,
) : AnthropicContentBlock()

@Serializable
@SerialName("image")
data class AnthropicImageBlock(
    val source: AnthropicImageSource,
) : AnthropicContentBlock()

@Serializable
data class AnthropicImageSource(
    val type: String = "base64",
    @SerialName("media_type") val mediaType: String,
    val data: String,
)
