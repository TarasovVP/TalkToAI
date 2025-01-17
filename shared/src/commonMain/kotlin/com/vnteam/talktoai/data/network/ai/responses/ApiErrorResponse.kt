package com.vnteam.talktoai.data.network.ai.responses

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val error: ApiError?
)