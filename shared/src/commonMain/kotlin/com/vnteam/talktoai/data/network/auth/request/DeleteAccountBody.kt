package com.vnteam.talktoai.data.network.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountBody(
    val idToken: String? = null
)
