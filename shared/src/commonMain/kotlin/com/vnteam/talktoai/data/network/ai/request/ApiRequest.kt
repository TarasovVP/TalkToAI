package com.vnteam.talktoai.data.network.ai.request

import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest(
    val model: String,
    val messages: List<MessageApi>,
    val temperature: Float,
)