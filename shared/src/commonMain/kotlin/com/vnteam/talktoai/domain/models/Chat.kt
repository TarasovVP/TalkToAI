package com.vnteam.talktoai.domain.models

import kotlinx.serialization.Serializable
import kotlin.Long
import kotlin.String

@Serializable
 data class Chat(
   val id: Long,
   var name: String,
   val updated: Long,
   var listOrder: Long,
)
