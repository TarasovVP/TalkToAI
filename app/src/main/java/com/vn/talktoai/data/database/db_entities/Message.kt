package com.vn.talktoai.data.database.db_entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vn.talktoai.CommonExtensions.EMPTY
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Message(@PrimaryKey(autoGenerate = true) var messageId: Int = 0,
                   var chatId: Int = 0,
                   var author: String = String.EMPTY,
                   var message: String = String.EMPTY,
                   var createdAt: Long = 0): Parcelable