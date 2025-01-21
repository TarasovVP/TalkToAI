package com.vnteam.talktoai.data.network.auth.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ChangePasswordResponse(
    val localId: String? = null,
    val email: String? = null,
    val passwordHash: String? = null,
    val providerUserInfo: List<JsonObject>? = null,
    val idToken: String? = null,
    val refreshToken: String? = null,
    val expiresIn: String? = null
)