package com.vnteam.talktoai.domain.models

import com.vnteam.talktoai.domain.enums.ModelTier

data class AiModel(
    val id: String,
    val displayName: String,
    val tier: ModelTier,
    val supportsTemperature: Boolean = false,
    val supportsVision: Boolean = false,
    val contextWindow: Int = 0,
    val maxOutputTokens: Int = DEFAULT_MAX_OUTPUT_TOKENS,
    val enabled: Boolean = true,
) {
    companion object {
        const val DEFAULT_MAX_OUTPUT_TOKENS = 16000
    }
}
