package com.vnteam.talktoai.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class MessageContent {
    @Serializable
    @SerialName("text")
    data class Text(val text: String) : MessageContent()

    @Serializable
    @SerialName("image")
    data class Image(
        val base64Data: String,
        val mimeType: String,
    ) : MessageContent()
}
