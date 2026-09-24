package com.vnteam.talktoai.presentation.uimodels

import androidx.compose.runtime.mutableStateOf
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.models.MessageContent
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class MessageUI(
    var id: Long = 0,
    var chatId: Long = DEFAULT_CHAT_ID,
    var author: String = String.EMPTY,
    var message: String = String.EMPTY,
    var updatedAt: Long = 0,
    var status: MessageStatus = MessageStatus.REQUESTING,
    var errorMessage: String = String.EMPTY,
    var isTruncated: Boolean = false,
    var isComplete: Boolean = true,
    var inputTokens: Int? = null,
    var outputTokens: Int? = null,
    var cacheReadTokens: Int? = null,
    var cacheWriteTokens: Int? = null,
    @Transient var attachedImage: MessageContent.Image? = null,
) {
    var isCheckedToDelete = mutableStateOf(false)
}
