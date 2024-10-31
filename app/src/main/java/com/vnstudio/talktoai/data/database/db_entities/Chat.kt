package com.vnstudio.talktoai.data.database.db_entities

import android.os.Parcelable
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import kotlinx.serialization.Serializable

@Serializable
data class Chat(
   var id: Long = DEFAULT_CHAT_ID,
    var name: String = String.EMPTY,
    var updated: Long = 0,
    var listOrder: Int = 0
)