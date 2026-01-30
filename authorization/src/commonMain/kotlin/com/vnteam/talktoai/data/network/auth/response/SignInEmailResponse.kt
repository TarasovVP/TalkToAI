package com.vnteam.talktoai.data.network.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class SignInEmailResponse(
    val idToken: String? = null,
    val email: String? = null,
    val refreshToken: String? = null,
    val expiresIn: String? = null,
    val localId: String? = null,
    val registered: Boolean? = null,
)