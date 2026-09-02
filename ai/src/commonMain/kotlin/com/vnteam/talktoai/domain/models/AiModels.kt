package com.vnteam.talktoai.domain.models

import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.enums.ModelTier

object AiModels {
    val OPENAI = listOf(
        AiModel("gpt-5.6-luna",  "GPT-5.6 Luna",  ModelTier.FAST),
        AiModel("gpt-5.6-terra", "GPT-5.6 Terra", ModelTier.BALANCED),
        AiModel("gpt-5.6-sol",   "GPT-5.6 Sol",   ModelTier.POWERFUL),
    )

    val ANTHROPIC = listOf(
        AiModel("claude-haiku-4-5-20251001", "Claude Haiku 4.5", ModelTier.FAST, supportsTemperature = true),
        AiModel("claude-sonnet-5",           "Claude Sonnet 5",  ModelTier.BALANCED),
        AiModel("claude-opus-4-8",           "Claude Opus 4.8",  ModelTier.POWERFUL),
    )

    fun forProvider(providerType: AiProviderType): List<AiModel> = when (providerType) {
        AiProviderType.OPENAI -> OPENAI
        AiProviderType.ANTHROPIC -> ANTHROPIC
    }

    fun balancedFor(providerType: AiProviderType): AiModel =
        forProvider(providerType).first { it.tier == ModelTier.BALANCED }
}
