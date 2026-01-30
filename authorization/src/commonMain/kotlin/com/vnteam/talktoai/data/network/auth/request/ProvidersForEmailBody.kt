package com.vnteam.talktoai.data.network.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class ProvidersForEmailBody(
    val identifier: String? = null,
    val continueUri: String? = null,
)