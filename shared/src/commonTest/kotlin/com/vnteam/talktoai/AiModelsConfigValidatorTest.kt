package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.firestore.FirestoreDocument
import com.vnteam.talktoai.data.network.firestore.toRawAiModelsConfig
import com.vnteam.talktoai.domain.aimodels.AiModelsCacheCodec
import com.vnteam.talktoai.domain.aimodels.AiModelsConfigResult
import com.vnteam.talktoai.domain.aimodels.AiModelsConfigValidator
import com.vnteam.talktoai.domain.aimodels.RawAiModel
import com.vnteam.talktoai.domain.aimodels.RawAiModelsConfig
import com.vnteam.talktoai.domain.aimodels.formatContextUsage
import com.vnteam.talktoai.domain.aimodels.lastAssistantInputTokens
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.enums.ModelTier
import com.vnteam.talktoai.domain.models.Message
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal fun raw(
    id: String,
    provider: String = "OPENAI",
    tier: String = "BALANCED",
    maxOutputTokens: Long? = 16000,
    contextWindow: Long? = 100_000,
    enabled: Boolean? = true,
    displayName: String? = id,
) = RawAiModel(
    id = id,
    displayName = displayName,
    provider = provider,
    tier = tier,
    supportsTemperature = false,
    supportsVision = true,
    contextWindow = contextWindow,
    maxOutputTokens = maxOutputTokens,
    enabled = enabled,
)

internal fun validConfig(schemaVersion: Long = 1, extra: List<RawAiModel> = emptyList()) = RawAiModelsConfig(
    schemaVersion = schemaVersion,
    models = listOf(
        raw("gpt-a", "OPENAI", "FAST"),
        raw("claude-a", "ANTHROPIC", "BALANCED"),
    ) + extra,
)

class AiModelsConfigValidatorTest {

    @Test
    fun validConfigIsApplied() {
        val result = assertIs<AiModelsConfigResult.Valid>(AiModelsConfigValidator.validate(validConfig()))
        assertEquals(listOf("gpt-a"), result.models[AiProviderType.OPENAI]!!.map { it.id })
        assertEquals(listOf("claude-a"), result.models[AiProviderType.ANTHROPIC]!!.map { it.id })
    }

    @Test
    fun unsupportedSchemaVersionIsRejected() {
        assertIs<AiModelsConfigResult.Rejected>(AiModelsConfigValidator.validate(validConfig(schemaVersion = 3)))
    }

    @Test
    fun modelWithMissingRequiredFieldIsDroppedOthersKept() {
        val broken = raw("gpt-broken", displayName = null)
        val unknownProvider = raw("x", provider = "MISTRAL")
        val unknownTier = raw("y", tier = "HUGE")
        val result = assertIs<AiModelsConfigResult.Valid>(
            AiModelsConfigValidator.validate(validConfig(extra = listOf(broken, unknownProvider, unknownTier)))
        )
        assertEquals(listOf("gpt-a"), result.models[AiProviderType.OPENAI]!!.map { it.id })
    }

    @Test
    fun missingOrInvalidMaxOutputTokensKeepsModelWith16000() {
        val zero = raw("gpt-zero", tier = "POWERFUL", maxOutputTokens = 0)
        val absent = raw("gpt-absent", tier = "BALANCED", maxOutputTokens = null)
        val result = assertIs<AiModelsConfigResult.Valid>(
            AiModelsConfigValidator.validate(validConfig(extra = listOf(zero, absent)))
        )
        val byId = result.models[AiProviderType.OPENAI]!!.associateBy { it.id }
        assertEquals(16000, byId["gpt-zero"]!!.maxOutputTokens)
        assertEquals(16000, byId["gpt-absent"]!!.maxOutputTokens)
        assertTrue(result.warnings.any { "gpt-zero" in it })
    }

    @Test
    fun nonPositiveContextWindowMeansUnknownAndKeepsModel() {
        val result = assertIs<AiModelsConfigResult.Valid>(
            AiModelsConfigValidator.validate(validConfig(extra = listOf(raw("gpt-ctx", contextWindow = 0))))
        )
        assertEquals(0, result.models[AiProviderType.OPENAI]!!.first { it.id == "gpt-ctx" }.contextWindow)
    }

    @Test
    fun emptyProviderAfterFilteringRejectsWholeConfig() {
        val config = RawAiModelsConfig(1, listOf(raw("gpt-a"), raw("claude-a", "ANTHROPIC", enabled = false)))
        assertIs<AiModelsConfigResult.Rejected>(AiModelsConfigValidator.validate(config))
    }

    @Test
    fun disabledModelsAreExcluded() {
        val result = assertIs<AiModelsConfigResult.Valid>(
            AiModelsConfigValidator.validate(validConfig(extra = listOf(raw("gpt-off", tier = "POWERFUL", enabled = false))))
        )
        assertTrue(result.models[AiProviderType.OPENAI]!!.none { it.id == "gpt-off" })
    }

    @Test
    fun modelsAreOrderedByTier() {
        val result = assertIs<AiModelsConfigResult.Valid>(
            AiModelsConfigValidator.validate(
                validConfig(extra = listOf(raw("gpt-p", tier = "POWERFUL"), raw("gpt-b", tier = "BALANCED")))
            )
        )
        assertEquals(
            listOf(ModelTier.FAST, ModelTier.BALANCED, ModelTier.POWERFUL),
            result.models[AiProviderType.OPENAI]!!.map { it.tier },
        )
    }

    @Test
    fun firestoreDocumentWithUnknownFieldsParsesAndValidates() {
        val body = """
        {"name":"projects/p/databases/(default)/documents/config/aiModels","fields":{
          "schemaVersion":{"integerValue":"2"},
          "futureTopLevel":{"stringValue":"ignored"},
          "tiers":{"mapValue":{"fields":{
            "BALANCED":{"mapValue":{"fields":{
              "OPENAI":{"mapValue":{"fields":{
                "id":{"stringValue":"gpt-a"},
                "displayName":{"stringValue":"GPT A"},
                "supportsTemperature":{"booleanValue":false},"supportsVision":{"booleanValue":true},
                "contextWindow":{"integerValue":"1050000"},"maxOutputTokens":{"integerValue":"12000"},
                "enabled":{"booleanValue":true},"newField":{"arrayValue":{"values":[]}}}}}
            }}},
            "FAST":{"mapValue":{"fields":{
              "ANTHROPIC":{"mapValue":{"fields":{
                "id":{"stringValue":"claude-a"},
                "displayName":{"stringValue":"Claude A"},
                "supportsTemperature":{"booleanValue":true},"supportsVision":{"booleanValue":true},
                "contextWindow":{"integerValue":"200000"},"enabled":{"booleanValue":true}}}}
            }}}
          }}}}}
        """.trimIndent()
        val doc = Json { ignoreUnknownKeys = true }.decodeFromString(FirestoreDocument.serializer(), body)
        val result = assertIs<AiModelsConfigResult.Valid>(AiModelsConfigValidator.validate(doc.toRawAiModelsConfig()))
        assertEquals(12000, result.models[AiProviderType.OPENAI]!!.single().maxOutputTokens)
        assertEquals(1_050_000, result.models[AiProviderType.OPENAI]!!.single().contextWindow)
        assertEquals(16000, result.models[AiProviderType.ANTHROPIC]!!.single().maxOutputTokens)
    }

    @Test
    fun firestoreDocumentWithProviderMissingFromOneTierOmitsItFromThatTierOnly() {
        val body = """
        {"name":"projects/p/databases/(default)/documents/config/aiModels","fields":{
          "schemaVersion":{"integerValue":"2"},
          "tiers":{"mapValue":{"fields":{
            "FAST":{"mapValue":{"fields":{
              "OPENAI":{"mapValue":{"fields":{
                "id":{"stringValue":"gpt-fast"},"displayName":{"stringValue":"GPT Fast"},
                "supportsTemperature":{"booleanValue":false},"supportsVision":{"booleanValue":true},
                "contextWindow":{"integerValue":"100000"},"maxOutputTokens":{"integerValue":"16000"},
                "enabled":{"booleanValue":true}}}}
            }}},
            "BALANCED":{"mapValue":{"fields":{
              "OPENAI":{"mapValue":{"fields":{
                "id":{"stringValue":"gpt-balanced"},"displayName":{"stringValue":"GPT Balanced"},
                "supportsTemperature":{"booleanValue":false},"supportsVision":{"booleanValue":true},
                "contextWindow":{"integerValue":"100000"},"maxOutputTokens":{"integerValue":"16000"},
                "enabled":{"booleanValue":true}}}},
              "ANTHROPIC":{"mapValue":{"fields":{
                "id":{"stringValue":"claude-balanced"},"displayName":{"stringValue":"Claude Balanced"},
                "supportsTemperature":{"booleanValue":false},"supportsVision":{"booleanValue":true},
                "contextWindow":{"integerValue":"100000"},"maxOutputTokens":{"integerValue":"16000"},
                "enabled":{"booleanValue":true}}}}
            }}}
          }}}}}
        """.trimIndent()
        val doc = Json { ignoreUnknownKeys = true }.decodeFromString(FirestoreDocument.serializer(), body)
        val result = assertIs<AiModelsConfigResult.Valid>(AiModelsConfigValidator.validate(doc.toRawAiModelsConfig()))
        assertEquals(listOf("gpt-fast", "gpt-balanced"), result.models[AiProviderType.OPENAI]!!.map { it.id })
        assertEquals(listOf("claude-balanced"), result.models[AiProviderType.ANTHROPIC]!!.map { it.id })
    }

    @Test
    fun firestoreDocumentWhereOneProviderHasNoModelsInAnyTierIsRejected() {
        val body = """
        {"name":"projects/p/databases/(default)/documents/config/aiModels","fields":{
          "schemaVersion":{"integerValue":"2"},
          "tiers":{"mapValue":{"fields":{
            "BALANCED":{"mapValue":{"fields":{
              "OPENAI":{"mapValue":{"fields":{
                "id":{"stringValue":"gpt-balanced"},"displayName":{"stringValue":"GPT Balanced"},
                "supportsTemperature":{"booleanValue":false},"supportsVision":{"booleanValue":true},
                "contextWindow":{"integerValue":"100000"},"maxOutputTokens":{"integerValue":"16000"},
                "enabled":{"booleanValue":true}}}}
            }}}
          }}}}}
        """.trimIndent()
        val doc = Json { ignoreUnknownKeys = true }.decodeFromString(FirestoreDocument.serializer(), body)
        assertIs<AiModelsConfigResult.Rejected>(AiModelsConfigValidator.validate(doc.toRawAiModelsConfig()))
    }

    @Test
    fun cacheRoundTripsThroughValidator() {
        val valid = assertIs<AiModelsConfigResult.Valid>(AiModelsConfigValidator.validate(validConfig()))
        val encoded = AiModelsCacheCodec.encode(valid.schemaVersion, valid.models)
        val decoded = AiModelsCacheCodec.decode(encoded)!!
        val again = assertIs<AiModelsConfigResult.Valid>(AiModelsConfigValidator.validate(decoded))
        assertEquals(valid.models, again.models)
    }

    @Test
    fun contextUsageFormatting() {
        assertEquals("12 400", formatContextUsage(12_400, 0))
        assertEquals("12 400 / 200 000 (6%)", formatContextUsage(12_400, 200_000))
        assertEquals("100 / 1 000 000 (<1%)", formatContextUsage(100, 1_000_000))
    }

    @Test
    fun lastAssistantInputTokensSkipsUserAndMessagesWithoutUsage() {
        val messages = listOf(
            Message(author = "claude", inputTokens = 100),
            Message(author = Constants.MESSAGE_ROLE_ME),
            Message(author = "claude", inputTokens = 250),
            Message(author = "claude", inputTokens = null),
        )
        assertEquals(250, lastAssistantInputTokens(messages))
        assertNull(lastAssistantInputTokens(listOf(Message(author = Constants.MESSAGE_ROLE_ME))))
    }
}
