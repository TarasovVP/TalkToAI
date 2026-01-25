package com.vnteam.talktoai.data.network.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordResponse(
    val email: String? = null,
)