package com.vnteam.talktoai.data.network.ai.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val id: String?,
    @SerialName("object") val chatObject: String?,
    val created: String?,
    val model: String?,
    val usage: Usage?,
    val choices: List<Choice>?,
)