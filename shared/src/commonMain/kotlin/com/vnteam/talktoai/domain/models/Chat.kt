package com.vnteam.talktoai.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: Long? = null,
    var name: String? = null,
    val updated: Long? = null,
    var listOrder: Long? = null,
)
