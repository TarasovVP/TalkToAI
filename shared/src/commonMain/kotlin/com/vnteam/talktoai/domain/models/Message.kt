package com.vnteam.talktoai.domain.models

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.domain.enums.MessageStatus
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    var id: Long? = null,
    var chatId: Long? = null,
    var author: String? = null,
    var message: String? = null,
    var updatedAt: Long? = null,
    var status: MessageStatus? = null,
    var errorMessage: String? = String.EMPTY,
    var truncated: Boolean = false,
)