package com.vn.talktoai.domain

import com.vn.talktoai.domain.models.MessageApi

data class ApiRequest(val model: String,
                      val messageApis: List<MessageApi>,
                      val temperature: Float)