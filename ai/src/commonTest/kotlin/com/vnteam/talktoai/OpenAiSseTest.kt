package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.openai.processOpenAiSseChannel
import com.vnteam.talktoai.data.network.ai.openai.response.OpenAiSseEvent
import com.vnteam.talktoai.data.network.ai.openai.response.parseOpenAiSseEvent
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest

class OpenAiSseTest {

    private val firstChunk =
        """{"id":"chatcmpl-1","object":"chat.completion.chunk","created":1,"model":"gpt-5.6-terra","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"role":"assistant","content":"","refusal":null},"finish_reason":null}],"obfuscation":"ctJejILkAk"}"""
    private val textChunk1 =
        """{"id":"chatcmpl-1","object":"chat.completion.chunk","created":1,"model":"gpt-5.6-terra","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"content":"Hello"},"finish_reason":null}],"obfuscation":"AJB0ubo"}"""
    private val textChunk2 =
        """{"id":"chatcmpl-1","object":"chat.completion.chunk","created":1,"model":"gpt-5.6-terra","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"content":"!"},"finish_reason":null}],"obfuscation":"QO10IIVyP9S"}"""
    private val finalChunk =
        """{"id":"chatcmpl-1","object":"chat.completion.chunk","created":1,"model":"gpt-5.6-terra","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{},"finish_reason":"stop"}],"obfuscation":"iuaIFo"}"""

    private fun sseLines(vararg dataLines: String): ByteReadChannel {
        val sb = StringBuilder()
        for (line in dataLines) {
            sb.append("data: $line\n")
            sb.append("\n")
        }
        return ByteReadChannel(sb.toString().encodeToByteArray())
    }

    private suspend fun collect(channel: ByteReadChannel, initialModel: String) =
        flow { processOpenAiSseChannel(channel, initialModel) }.toList()

    @Test
    fun doneMarkerIsRecognizedAndDoesNotThrow() {
        val event = parseOpenAiSseEvent("[DONE]")
        assertIs<OpenAiSseEvent.Done>(event)
    }

    @Test
    fun chunkWithObfuscationFieldParsesCorrectly() {
        val event = parseOpenAiSseEvent(textChunk1)
        val chunk = assertIs<OpenAiSseEvent.Chunk>(event)
        assertEquals("gpt-5.6-terra", chunk.model)
        assertEquals("Hello", chunk.contentDelta)
    }

    @Test
    fun firstChunkEmptyContentIsNotTreatedAsAbsent() {
        val event = parseOpenAiSseEvent(firstChunk)
        val chunk = assertIs<OpenAiSseEvent.Chunk>(event)
        assertEquals("", chunk.contentDelta)
        assertNull(chunk.finishReason)
    }

    @Test
    fun lastChunkWithEmptyDeltaAndFinishReasonIsRecognized() {
        val event = parseOpenAiSseEvent(finalChunk)
        val chunk = assertIs<OpenAiSseEvent.Chunk>(event)
        assertNull(chunk.contentDelta)
        assertEquals("stop", chunk.finishReason)
    }

    @Test
    fun seriesOfChunksProducesMultipleEmitsWithGrowingText() = runTest {
        val channel = sseLines(firstChunk, textChunk1, textChunk2, finalChunk, "[DONE]")
        val results = collect(channel, "fallback-model")
        val successes = results.filterIsInstance<Result.Success<AiTextResponse>>()
        assertTrue(successes.size >= 2, "Expected multiple Success emits, got ${successes.size}")
        val last = successes.last()
        assertEquals("gpt-5.6-terra", last.data!!.model)
        assertEquals("Hello!", last.data!!.content)
    }

    @Test
    fun firstEmptyContentChunkDoesNotAppendText() = runTest {
        val channel = sseLines(firstChunk, textChunk1, finalChunk, "[DONE]")
        val results = collect(channel, "fallback-model")
        val successes = results.filterIsInstance<Result.Success<AiTextResponse>>()
        successes.forEach { assertTrue(!it.data!!.content.startsWith(" ")) }
        assertEquals("Hello", successes.last().data!!.content)
    }

    @Test
    fun connectionClosedWithoutDoneEmitsFailure() = runTest {
        val channel = sseLines(firstChunk, textChunk1)
        val results = collect(channel, "fallback-model")
        assertIs<Result.Failure>(results.last())
    }
}
