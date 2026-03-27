package com.vnteam.talktoai.data.network.ai.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelsResponse(
    @SerialName("object") val listObject: String? = null,
    val data: List<ModelData>? = null,
)

@Serializable
data class ModelData(
    val id: String,
    @SerialName("object") val modelObject: String? = null,
    val created: Long? = null,
    @SerialName("owned_by") val ownedBy: String? = null,
)
