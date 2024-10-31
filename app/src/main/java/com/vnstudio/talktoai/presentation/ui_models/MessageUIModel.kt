package com.vnstudio.talktoai.presentation.ui_models

import androidx.compose.runtime.mutableStateOf
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.domain.enums.MessageStatus
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import kotlinx.serialization.Serializable

@Serializable
data class MessageUIModel(
    var id: Long = 0,
    var chatId: Long = DEFAULT_CHAT_ID,
    var author: String = String.EMPTY,
    var message: String = String.EMPTY,
    var updatedAt: Long = 0,
    var status: MessageStatus = MessageStatus.REQUESTING,
    var errorMessage: String = String.EMPTY,
    var isTruncated: Boolean = false
) {

    var isCheckedToDelete = mutableStateOf(false)
}