package com.vnteam.talktoai.data.network.ai.openai.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageApi(
    val role: String?,
    val content: String?,
)