package com.vnteam.talktoai.domain.models

import com.vnteam.talktoai.domain.enums.MessageStatus
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Message(
    var id: Long? = null,
    var chatId: Long? = null,
    var author: String? = null,
    var content: List<MessageContent> = emptyList(),
    var updatedAt: Long? = null,
    var status: MessageStatus? = null,
    var errorMessage: String? = "",
    var truncated: Boolean = false,
) {
    @Transient
    val message: String = content.filterIsInstance<MessageContent.Text>().joinToString("") { it.text }
}
