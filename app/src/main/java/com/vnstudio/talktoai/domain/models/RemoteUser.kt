package com.vnstudio.talktoai.domain.models

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import kotlinx.serialization.Serializable

@Serializable
data class RemoteUser(
    var chats: ArrayList<Chat> = arrayListOf(),
    var messages: ArrayList<Message> = arrayListOf(),
    var isReviewVoted: Boolean = false,
)