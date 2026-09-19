package com.vnteam.talktoai.data.network.ai.openai.request

import kotlinx.serialization.Serializable

@Serializable
data class OpenAiMessage(
    val role: String?,
    val content: List<OpenAiContentBlock>,
)
