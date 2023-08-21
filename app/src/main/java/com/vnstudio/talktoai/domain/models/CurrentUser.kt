package com.vnstudio.talktoai.domain.models

import android.os.Parcelable
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentUser(
    var chatList: ArrayList<Chat> = arrayListOf(),
    var messageList: ArrayList<Message> = arrayListOf(),
    var isReviewVoted: Boolean = false
) : Parcelable