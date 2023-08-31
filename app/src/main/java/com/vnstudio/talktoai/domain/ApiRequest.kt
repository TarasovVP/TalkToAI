package com.vnstudio.talktoai.domain

import com.vnstudio.talktoai.domain.models.MessageApi

data class ApiRequest(
    val model: String,
    val messages: List<MessageApi>,
    val temperature: Float,
)