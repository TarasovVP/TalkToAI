package com.vnteam.talktoai.data.network.ai.responses

import com.vnteam.talktoai.data.network.ai.request.MessageApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val message: MessageApi?,
    @SerialName("finish_reason") val finishReason: String?,
    val index: Int?,
)