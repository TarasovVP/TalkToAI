package com.vnteam.talktoai.data.network.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class SignUpEmailResponse(
    val idToken: String? = null,
    val email: String? = null,
    val refreshToken: String? = null,
    val expiresIn: String? = null,
    val localId: String? = null,
)