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
    @PrimaryKey var id: Long = 0,
    var chatId: Long = 0,
    var author: String = String.EMPTY,
    var message: String = String.EMPTY,
    var updatedAt: Long = 0,
    var status: MessageStatus? = null,
    var errorMessage: String = String.EMPTY
) : Parcelable