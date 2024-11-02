package com.vnteam.talktoai.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class RemoteUser(
    var chats: ArrayList<Chat> = arrayListOf(),
    var messages: ArrayList<Message> = arrayListOf(),
    var isReviewVoted: Boolean = false,
)