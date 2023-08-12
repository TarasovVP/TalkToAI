package com.vn.talktoai.domain

import com.vn.talktoai.domain.models.MessageApi

data class ApiRequest(val model: String,
                      val messages: List<MessageApi>,
                      val temperature: Float)