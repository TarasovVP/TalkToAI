package com.vnteam.talktoai

import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.enums.ModelTier
import com.vnteam.talktoai.domain.models.AiModels
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AiModelsTest {

    @Test
    fun `each ModelTier appears exactly once in OPENAI list`() {
        val tiers = AiModels.OPENAI.map { it.tier }
        assertEquals(ModelTier.entries.size, tiers.size)
        ModelTier.entries.forEach { tier ->
            assertEquals("OPENAI: tier $tier should appear exactly once", 1, tiers.count { it == tier })
        }
    }

    @Test
    fun `each ModelTier appears exactly once in ANTHROPIC list`() {
        val tiers = AiModels.ANTHROPIC.map { it.tier }
        assertEquals(ModelTier.entries.size, tiers.size)
        ModelTier.entries.forEach { tier ->
            assertEquals("ANTHROPIC: tier $tier should appear exactly once", 1, tiers.count { it == tier })
        }
    }

    @Test
    fun `balancedFor returns BALANCED model for OPENAI`() {
        val model = AiModels.balancedFor(AiProviderType.OPENAI)
        assertEquals(ModelTier.BALANCED, model.tier)
    }

    @Test
    fun `balancedFor returns BALANCED model for ANTHROPIC`() {
        val model = AiModels.balancedFor(AiProviderType.ANTHROPIC)
        assertEquals(ModelTier.BALANCED, model.tier)
    }

    @Test
    fun `all model ids are non-empty`() {
        (AiModels.OPENAI + AiModels.ANTHROPIC).forEach { model ->
            assertTrue("id should not be blank for ${model.displayName}", model.id.isNotBlank())
        }
    }

    @Test
    fun `all model displayNames are non-empty`() {
        (AiModels.OPENAI + AiModels.ANTHROPIC).forEach { model ->
            assertTrue("displayName should not be blank for ${model.id}", model.displayName.isNotBlank())
        }
    }

    @Test
    fun `forProvider returns correct list for OPENAI`() {
        assertEquals(AiModels.OPENAI, AiModels.forProvider(AiProviderType.OPENAI))
    }

    @Test
    fun `forProvider returns correct list for ANTHROPIC`() {
        assertEquals(AiModels.ANTHROPIC, AiModels.forProvider(AiProviderType.ANTHROPIC))
    }
}
