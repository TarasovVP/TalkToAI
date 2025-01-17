package com.vnteam.talktoai.data.network.ai.responses

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val message: String?,
    val type: String?,
    val param: String?,
    val code: String?
)