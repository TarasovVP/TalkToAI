package com.vnteam.talktoai.data.network.ai.openai.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class OpenAiContentBlock

@Serializable
@SerialName("text")
data class OpenAiTextBlock(val text: String) : OpenAiContentBlock()

@Serializable
@SerialName("image_url")
data class OpenAiImageBlock(
    @SerialName("image_url") val imageUrl: OpenAiImageUrl,
) : OpenAiContentBlock()

@Serializable
data class OpenAiImageUrl(val url: String)
