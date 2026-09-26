package com.vnteam.talktoai.domain.aimodels

import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.AiModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object AiModelsCacheCodec {

    private val json = Json { ignoreUnknownKeys = true }

    @Serializable
    private data class Envelope(
        val schemaVersion: Long? = null,
        val models: List<CachedModel> = emptyList(),
    )

    @Serializable
    private data class CachedModel(
        val id: String = "",
        val provider: String? = null,
        val displayName: String? = null,
        val tier: String? = null,
        val supportsTemperature: Boolean? = null,
        val supportsVision: Boolean? = null,
        val contextWindow: Long? = null,
        val maxOutputTokens: Long? = null,
    )

    fun encode(schemaVersion: Int, models: Map<AiProviderType, List<AiModel>>): String =
        json.encodeToString(
            Envelope.serializer(),
            Envelope(
                schemaVersion = schemaVersion.toLong(),
                models = models.flatMap { (provider, list) ->
                    list.map {
                        CachedModel(
                            id = it.id,
                            provider = provider.name,
                            displayName = it.displayName,
                            tier = it.tier.name,
                            supportsTemperature = it.supportsTemperature,
                            supportsVision = it.supportsVision,
                            contextWindow = it.contextWindow.toLong(),
                            maxOutputTokens = it.maxOutputTokens.toLong(),
                        )
                    }
                },
            ),
        )

    fun decode(cached: String): RawAiModelsConfig? = runCatching {
        val envelope = json.decodeFromString(Envelope.serializer(), cached)
        RawAiModelsConfig(
            schemaVersion = envelope.schemaVersion,
            models = envelope.models.map {
                RawAiModel(
                    id = it.id,
                    displayName = it.displayName,
                    provider = it.provider,
                    tier = it.tier,
                    supportsTemperature = it.supportsTemperature,
                    supportsVision = it.supportsVision,
                    contextWindow = it.contextWindow,
                    maxOutputTokens = it.maxOutputTokens,
                    enabled = true,
                )
            },
        )
    }.getOrNull()
}
