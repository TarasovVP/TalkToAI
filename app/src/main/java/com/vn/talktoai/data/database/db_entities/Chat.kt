package com.vn.talktoai.data.database.db_entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vn.talktoai.CommonExtensions.EMPTY
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Chat(@PrimaryKey(autoGenerate = true) var chatId: Int = 0,
               var name: String = String.EMPTY): Parcelable