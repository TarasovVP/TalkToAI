package com.vnstudio.talktoai.data.database.db_entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.domain.enums.MessageStatus
import com.vnstudio.talktoai.infrastructure.Constants.MESSAGES
import kotlinx.parcelize.Parcelize

@Entity(tableName = MESSAGES)
@Parcelize
data class Message(
    @PrimaryKey var id: Long? = null,
    var chatId: Long? = null,
    var author: String? = null,
    var message: String? = null,
    var updatedAt: Long? = null,
    var status: MessageStatus? = null,
    var errorMessage: String? = String.EMPTY,
    var truncated: Boolean = false
) : Parcelable