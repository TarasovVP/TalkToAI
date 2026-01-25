package com.vnteam.talktoai.presentation.uimodels

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import kotlinx.serialization.Serializable

@Serializable
data class ChatUI(
    val id: Long = DEFAULT_CHAT_ID,
    val name: String = String.EMPTY,
    val updated: Long = 0,
    val listOrder: Long = 0,
)
