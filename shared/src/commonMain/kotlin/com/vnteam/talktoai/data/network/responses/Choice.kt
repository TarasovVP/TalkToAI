package com.vnteam.talktoai.data.network.responses

import com.vnteam.talktoai.data.network.request.MessageApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val message: MessageApi?,
    @SerialName("finish_reason") val finishReason: String?,
    val index: Int?,
)