package com.vnstudio.talktoai.data.database.db_entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.infrastructure.Constants.CHATS
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import kotlinx.parcelize.Parcelize

@Entity(tableName = CHATS)
@Parcelize
data class Chat(
    @PrimaryKey var id: Long = DEFAULT_CHAT_ID,
    var name: String = String.EMPTY,
    var updated: Long = 0,
) : Parcelable