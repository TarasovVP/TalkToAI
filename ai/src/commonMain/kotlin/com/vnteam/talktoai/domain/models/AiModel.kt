package com.vnteam.talktoai.domain.models

import com.vnteam.talktoai.domain.enums.ModelTier

data class AiModel(
    val id: String,
    val displayName: String,
    val tier: ModelTier,
    val supportsTemperature: Boolean = false,
)
