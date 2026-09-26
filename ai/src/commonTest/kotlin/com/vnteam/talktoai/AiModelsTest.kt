package com.vnteam.talktoai

import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.enums.ModelTier
import com.vnteam.talktoai.domain.models.AiModel
import com.vnteam.talktoai.domain.models.AiModels
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AiModelsTest {

    @Test
    fun eachModelTierAppearsExactlyOnceInOpenAiList() {
        val tiers = AiModels.OPENAI.map { it.tier }
        assertEquals(ModelTier.entries.size, tiers.size)
        ModelTier.entries.forEach { tier ->
            assertEquals(1, tiers.count { it == tier })
        }
    }

    @Test
    fun eachModelTierAppearsExactlyOnceInAnthropicList() {
        val tiers = AiModels.ANTHROPIC.map { it.tier }
        assertEquals(ModelTier.entries.size, tiers.size)
        ModelTier.entries.forEach { tier ->
            assertEquals(1, tiers.count { it == tier })
        }
    }

    @Test
    fun balancedForReturnsBalancedModelForOpenAi() {
        val model = AiModels.balancedFor(AiProviderType.OPENAI)
        assertEquals(ModelTier.BALANCED, model.tier)
    }

    @Test
    fun balancedForReturnsBalancedModelForAnthropic() {
        val model = AiModels.balancedFor(AiProviderType.ANTHROPIC)
        assertEquals(ModelTier.BALANCED, model.tier)
    }

    @Test
    fun allModelIdsAreNonEmpty() {
        (AiModels.OPENAI + AiModels.ANTHROPIC).forEach { model ->
            assertTrue(model.id.isNotBlank())
        }
    }

    @Test
    fun allModelDisplayNamesAreNonEmpty() {
        (AiModels.OPENAI + AiModels.ANTHROPIC).forEach { model ->
            assertTrue(model.displayName.isNotBlank())
        }
    }

    @Test
    fun forProviderReturnsCorrectListForOpenAi() {
        assertEquals(AiModels.OPENAI, AiModels.forProvider(AiProviderType.OPENAI))
    }

    @Test
    fun forProviderReturnsCorrectListForAnthropic() {
        assertEquals(AiModels.ANTHROPIC, AiModels.forProvider(AiProviderType.ANTHROPIC))
    }

    @Test
    fun onlyHaikuSupportsTemperatureInAnthropicList() {
        val temperatureModels = AiModels.ANTHROPIC.filter { it.supportsTemperature }
        assertEquals(1, temperatureModels.size)
        assertEquals("claude-haiku-4-5-20251001", temperatureModels.first().id)
    }

    @Test
    fun noOpenAiModelSupportsTemperature() {
        assertTrue(AiModels.OPENAI.none { it.supportsTemperature })
    }

    @Test
    fun balancedForReadsFromActiveListNotHardcode() {
        try {
            val custom = AiModel("custom-balanced", "Custom", ModelTier.BALANCED)
            AiModels.replace(mapOf(AiProviderType.OPENAI to listOf(custom)))
            assertEquals("custom-balanced", AiModels.balancedFor(AiProviderType.OPENAI).id)
            assertEquals(listOf(custom), AiModels.forProvider(AiProviderType.OPENAI))
        } finally {
            AiModels.reset()
        }
    }

    @Test
    fun balancedForFallsBackToFirstModelWhenNoBalancedTier() {
        try {
            val fast = AiModel("only-fast", "Fast", ModelTier.FAST)
            AiModels.replace(mapOf(AiProviderType.ANTHROPIC to listOf(fast)))
            assertEquals("only-fast", AiModels.balancedFor(AiProviderType.ANTHROPIC).id)
        } finally {
            AiModels.reset()
        }
    }

    @Test
    fun providerMissingFromActiveListFallsBackToHardcode() {
        try {
            AiModels.replace(mapOf(AiProviderType.OPENAI to listOf(AiModel("x", "X", ModelTier.BALANCED))))
            assertEquals(AiModels.ANTHROPIC, AiModels.forProvider(AiProviderType.ANTHROPIC))
        } finally {
            AiModels.reset()
        }
    }

    @Test
    fun hardcodedModelsCarryContextWindowAndMaxOutput() {
        (AiModels.OPENAI + AiModels.ANTHROPIC).forEach {
            assertTrue(it.contextWindow > 0, it.id)
            assertEquals(16000, it.maxOutputTokens, it.id)
        }
    }
}
