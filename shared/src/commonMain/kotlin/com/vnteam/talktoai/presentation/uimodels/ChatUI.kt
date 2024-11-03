package com.vnteam.talktoai.presentation.uimodels

import com.vnteam.talktoai.CommonExtensions.EMPTY
import kotlinx.serialization.Serializable
import kotlin.Long
import kotlin.String

@Serializable
 data class ChatUI(
   val id: Long = 0,
   val name: String = String.EMPTY,
   val updated: Long = 0,
   val listOrder: Long = 0
)
