package com.vnstudio.talktoai.data.network.models

import com.vnstudio.talktoai.domain.models.MessageApi
import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest(
    val model: String,
    val messages: List<MessageApi>,
    val temperature: Float,
)