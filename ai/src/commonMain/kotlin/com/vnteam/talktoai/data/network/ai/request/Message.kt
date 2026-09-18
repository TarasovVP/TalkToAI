package com.vnteam.talktoai.data.network.ai.request

import com.vnteam.talktoai.domain.models.MessageContent

data class Message(val role: String?, val content: List<MessageContent>)
