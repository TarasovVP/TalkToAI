package com.vnteam.talktoai

import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.resolveEffectiveProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProviderSelectionTest {

    @Test
    fun perChatProviderWinsOverGlobal_openai() {
        val result = resolveEffectiveProvider("OPENAI", AiProviderType.ANTHROPIC)
        assertEquals(AiProviderType.OPENAI, result)
    }

    @Test
    fun perChatProviderWinsOverGlobal_anthropic() {
        val result = resolveEffectiveProvider("ANTHROPIC", AiProviderType.OPENAI)
        assertEquals(AiProviderType.ANTHROPIC, result)
    }

    @Test
    fun nullChatProviderFallsBackToGlobal_anthropic() {
        val result = resolveEffectiveProvider(null, AiProviderType.ANTHROPIC)
        assertEquals(AiProviderType.ANTHROPIC, result)
    }

    @Test
    fun nullChatProviderFallsBackToGlobal_openai() {
        val result = resolveEffectiveProvider(null, AiProviderType.OPENAI)
        assertEquals(AiProviderType.OPENAI, result)
    }

    @Test
    fun invalidChatProviderStringFallsBackToGlobal() {
        val result = resolveEffectiveProvider("UNKNOWN_PROVIDER", AiProviderType.ANTHROPIC)
        assertEquals(AiProviderType.ANTHROPIC, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun globalProviderFlow_collectReceivesUpdatedValue() = runTest {
        val flow = MutableStateFlow(AiProviderType.OPENAI)
        var lastCollected: AiProviderType? = null

        val job = launch { flow.collect { lastCollected = it } }
        advanceUntilIdle()
        assertEquals(AiProviderType.OPENAI, lastCollected)

        flow.value = AiProviderType.ANTHROPIC
        advanceUntilIdle()
        assertEquals(AiProviderType.ANTHROPIC, lastCollected)

        job.cancel()
    }

    @Test
    fun globalProviderChange_affectsNullAiProviderChat() = runTest {
        val globalProviderFlow = MutableStateFlow(AiProviderType.OPENAI)
        val chatAiProvider: String? = null

        val effectiveBeforeChange = resolveEffectiveProvider(chatAiProvider, globalProviderFlow.value)
        assertEquals(AiProviderType.OPENAI, effectiveBeforeChange)

        globalProviderFlow.value = AiProviderType.ANTHROPIC

        val effectiveAfterChange = resolveEffectiveProvider(chatAiProvider, globalProviderFlow.value)
        assertEquals(AiProviderType.ANTHROPIC, effectiveAfterChange)
    }

    @Test
    fun globalProviderChange_doesNotAffectExplicitChatProvider() = runTest {
        val globalProviderFlow = MutableStateFlow(AiProviderType.OPENAI)
        val chatAiProvider = "ANTHROPIC"

        val effectiveBeforeChange = resolveEffectiveProvider(chatAiProvider, globalProviderFlow.value)
        assertEquals(AiProviderType.ANTHROPIC, effectiveBeforeChange)

        globalProviderFlow.value = AiProviderType.ANTHROPIC

        val effectiveAfterChange = resolveEffectiveProvider(chatAiProvider, globalProviderFlow.value)
        assertEquals(AiProviderType.ANTHROPIC, effectiveAfterChange)
    }
}
