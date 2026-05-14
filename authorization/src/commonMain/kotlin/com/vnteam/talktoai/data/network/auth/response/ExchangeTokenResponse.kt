package com.vnteam.talktoai.data.network.auth.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeTokenResponse(
    @SerialName("id_token") val idToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("user_id") val userId: String? = null,
)
