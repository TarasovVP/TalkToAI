package com.vnteam.talktoai.data.network.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthBody(
    val email: String? = null,
    val password: String? = null,
    val returnSecureToken: Boolean = true,
)