package com.vnteam.talktoai.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class MessageContent {
    @Serializable
    @SerialName("text")
    data class Text(val text: String) : MessageContent()

    @Serializable
    @SerialName("image")
    data class Image(
        @Transient val base64Data: String? = null,
        val mimeType: String,
        val storageKey: String? = null,
    ) : MessageContent()
}
