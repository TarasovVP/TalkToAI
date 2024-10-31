package com.vnstudio.talktoai.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val error: ApiError?
)