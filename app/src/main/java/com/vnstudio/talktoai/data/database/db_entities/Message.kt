package com.vnstudio.talktoai.data.database.db_entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.infrastructure.Constants
import com.vnstudio.talktoai.infrastructure.Constants.MESSAGES
import kotlinx.parcelize.Parcelize

@Entity(tableName = MESSAGES)
@Parcelize
data class Message(
    @PrimaryKey(autoGenerate = true) var messageId: Int = 0,
    var chatId: Int = 0,
    var author: String = String.EMPTY,
    var message: String = String.EMPTY,
    var createdAt: Long = 0,
) : Parcelable