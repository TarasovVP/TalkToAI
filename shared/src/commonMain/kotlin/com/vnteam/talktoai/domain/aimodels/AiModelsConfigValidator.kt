package com.vnteam.talktoai.domain.aimodels

import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.enums.ModelTier
import com.vnteam.talktoai.domain.models.AiModel

sealed class AiModelsConfigResult {
    data class Valid(
        val schemaVersion: Int,
        val models: Map<AiProviderType, List<AiModel>>,
        val warnings: List<String>,
    ) : AiModelsConfigResult()

    data class Rejected(val reason: String) : AiModelsConfigResult()
}

object AiModelsConfigValidator {

    const val SUPPORTED_SCHEMA_VERSION = 2

    fun validate(raw: RawAiModelsConfig): AiModelsConfigResult {
        val version = raw.schemaVersion
            ?: return AiModelsConfigResult.Rejected("schemaVersion is missing")
        if (version < 1) return AiModelsConfigResult.Rejected("invalid schemaVersion $version")
        if (version > SUPPORTED_SCHEMA_VERSION) {
            return AiModelsConfigResult.Rejected("unsupported schemaVersion $version, known $SUPPORTED_SCHEMA_VERSION")
        }

        val warnings = mutableListOf<String>()
        val parsed = raw.models.mapNotNull { model -> parseModel(model, warnings) }
        val enabled = parsed.filter { it.second.enabled }
        val byProvider = AiProviderType.entries.associateWith { provider ->
            enabled.filter { it.first == provider }
                .map { it.second }
                .sortedBy { it.tier.ordinal }
        }
        val empty = byProvider.filterValues { it.isEmpty() }.keys
        if (empty.isNotEmpty()) {
            return AiModelsConfigResult.Rejected("no valid models left for provider(s) $empty")
        }
        return AiModelsConfigResult.Valid(version.toInt(), byProvider, warnings)
    }

    private fun parseModel(raw: RawAiModel, warnings: MutableList<String>): Pair<AiProviderType, AiModel>? {
        fun drop(reason: String): Pair<AiProviderType, AiModel>? {
            warnings += "model '${raw.id}' dropped: $reason"
            return null
        }
        if (raw.id.isBlank()) return drop("blank id")
        val displayName = raw.displayName?.takeIf { it.isNotBlank() } ?: return drop("displayName is missing")
        val provider = AiProviderType.entries.firstOrNull { it.name == raw.provider }
            ?: return drop("unknown provider '${raw.provider}'")
        val tier = ModelTier.entries.firstOrNull { it.name == raw.tier }
            ?: return drop("unknown tier '${raw.tier}'")
        val supportsTemperature = raw.supportsTemperature ?: return drop("supportsTemperature is missing")
        val supportsVision = raw.supportsVision ?: return drop("supportsVision is missing")

        val maxOutput = raw.maxOutputTokens?.takeIf { it in 1..Int.MAX_VALUE }?.toInt()
        if (maxOutput == null) {
            warnings += "model '${raw.id}': maxOutputTokens=${raw.maxOutputTokens} is invalid, using ${AiModel.DEFAULT_MAX_OUTPUT_TOKENS}"
        }
        val contextWindow = raw.contextWindow?.takeIf { it in 1..Int.MAX_VALUE }?.toInt() ?: 0

        return provider to AiModel(
            id = raw.id,
            displayName = displayName,
            tier = tier,
            supportsTemperature = supportsTemperature,
            supportsVision = supportsVision,
            contextWindow = contextWindow,
            maxOutputTokens = maxOutput ?: AiModel.DEFAULT_MAX_OUTPUT_TOKENS,
            enabled = raw.enabled ?: true,
        )
    }
}
