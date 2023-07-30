package com.vn.talktoai

data class ApiRequest(val model: String,
                      val messages: List<Message>,
                      val temperature: Float)