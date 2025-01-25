package com.vnteam.talktoai.data.network.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class ProvidersForEmailResponse(
    val allProviders: List<String>? = null,
    val registered: Boolean? = null
)