package com.vn.talktoai.domain

import com.vn.talktoai.domain.models.Message

data class ApiRequest(val model: String,
                      val messages: List<Message>,
                      val temperature: Float)