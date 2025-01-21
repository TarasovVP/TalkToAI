package com.vnteam.talktoai.data.network.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordBody(
    val email: String? = null,
    val requestType: String? = null
)
