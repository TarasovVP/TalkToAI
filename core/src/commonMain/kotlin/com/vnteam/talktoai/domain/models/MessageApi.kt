package com.vnteam.talktoai.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class MessageApi(
    val role: String?,
    val content: String?,
)