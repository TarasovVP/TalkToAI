package com.vnstudio.talktoai.data.database.db_entities

import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.domain.enums.MessageStatus
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
    var truncated: Boolean = false
)