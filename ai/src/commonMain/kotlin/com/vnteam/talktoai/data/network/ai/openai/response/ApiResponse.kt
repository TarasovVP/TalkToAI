package com.vnteam.talktoai.data.network.ai.openai.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val id: String?,
    @SerialName("object") val chatObject: String?,
    val created: Long?,
    val model: String?,
    val usage: Usage?,
    val choices: List<Choice>?,
)
