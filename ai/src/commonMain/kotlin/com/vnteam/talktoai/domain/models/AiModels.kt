package com.vnteam.talktoai.domain.models

import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.enums.ModelTier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AiModels {
    val OPENAI = listOf(
        AiModel("gpt-5.6-luna",  "GPT-5.6 Luna",  ModelTier.FAST,     supportsVision = true, contextWindow = 1_050_000, maxOutputTokens = 16000),
        AiModel("gpt-5.6-terra", "GPT-5.6 Terra", ModelTier.BALANCED, supportsVision = true, contextWindow = 1_050_000, maxOutputTokens = 16000),
        AiModel("gpt-5.6-sol",   "GPT-5.6 Sol",   ModelTier.POWERFUL, supportsVision = true, contextWindow = 1_050_000, maxOutputTokens = 16000),
    )

    val ANTHROPIC = listOf(
        AiModel("claude-haiku-4-5-20251001", "Claude Haiku 4.5", ModelTier.FAST, supportsTemperature = true, supportsVision = true, contextWindow = 200_000, maxOutputTokens = 16000),
        AiModel("claude-sonnet-5",           "Claude Sonnet 5",  ModelTier.BALANCED, supportsVision = true, contextWindow = 1_000_000, maxOutputTokens = 16000),
        AiModel("claude-opus-4-8",           "Claude Opus 4.8",  ModelTier.POWERFUL, supportsVision = true, contextWindow = 1_000_000, maxOutputTokens = 16000),
    )

    private val _current = MutableStateFlow(
        mapOf(AiProviderType.OPENAI to OPENAI, AiProviderType.ANTHROPIC to ANTHROPIC)
    )
    val current: StateFlow<Map<AiProviderType, List<AiModel>>> = _current.asStateFlow()

    fun replace(models: Map<AiProviderType, List<AiModel>>) {
        _current.value = models
    }

    fun reset() {
        _current.value = mapOf(AiProviderType.OPENAI to OPENAI, AiProviderType.ANTHROPIC to ANTHROPIC)
    }

    fun forProvider(providerType: AiProviderType): List<AiModel> =
        _current.value[providerType]?.takeIf { it.isNotEmpty() } ?: hardcodedFor(providerType)

    fun balancedFor(providerType: AiProviderType): AiModel {
        val models = forProvider(providerType)
        return models.firstOrNull { it.tier == ModelTier.BALANCED }
            ?: models.firstOrNull()
            ?: hardcodedFor(providerType).first { it.tier == ModelTier.BALANCED }
    }

    fun find(providerType: AiProviderType, id: String): AiModel? =
        forProvider(providerType).firstOrNull { it.id == id }

    private fun hardcodedFor(providerType: AiProviderType): List<AiModel> = when (providerType) {
        AiProviderType.OPENAI -> OPENAI
        AiProviderType.ANTHROPIC -> ANTHROPIC
    }
}
