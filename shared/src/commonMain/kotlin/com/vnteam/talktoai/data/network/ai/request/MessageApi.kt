package com.vnteam.talktoai.data.network.ai.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageApi(
    val role: String?,
    val content: String?,
)