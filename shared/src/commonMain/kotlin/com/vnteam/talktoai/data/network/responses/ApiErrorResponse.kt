package com.vnteam.talktoai.data.network.responses

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val error: ApiError?
)