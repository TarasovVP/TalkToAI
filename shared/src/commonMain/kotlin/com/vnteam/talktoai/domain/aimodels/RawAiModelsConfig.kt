package com.vnteam.talktoai.domain.aimodels

data class RawAiModelsConfig(
    val schemaVersion: Long?,
    val models: List<RawAiModel>,
)

data class RawAiModel(
    val id: String,
    val displayName: String? = null,
    val provider: String? = null,
    val tier: String? = null,
    val supportsTemperature: Boolean? = null,
    val supportsVision: Boolean? = null,
    val contextWindow: Long? = null,
    val maxOutputTokens: Long? = null,
    val enabled: Boolean? = null,
)
