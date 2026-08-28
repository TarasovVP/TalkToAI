package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.anthropic.processSseChannel
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class AnthropicSseTest {

    // language=JSON
    private val messageStartData = """{"type":"message_start","message":{"id":"msg_1","type":"message","role":"assistant","model":"claude-sonnet-5","content":[],"stop_reason":null,"stop_sequence":null,"usage":{"input_tokens":10,"output_tokens":0}}}"""
    private val deltaData1 = """{"type":"content_block_delta","index":0,"delta":{"type":"text_delta","text":"Hello"}}"""
    private val deltaData2 = """{"type":"content_block_delta","index":0,"delta":{"type":"text_delta","text":", world"}}"""
    private val messageStopData = """{"type":"message_delta","delta":{"stop_reason":"end_turn"}}"""

    private fun sseStream(vararg events: Pair<String, String>): ByteReadChannel {
        val sb = StringBuilder()
        for ((eventType, data) in events) {
            sb.append("event: $eventType\n")
            sb.append("data: $data\n")
            sb.append("\n")
        }
        return ByteReadChannel(sb.toString().encodeToByteArray())
    }

    // Case 1: full happy path — MessageStop received → Success
    @Test
    fun fullStreamEmitsSuccess() = runTest {
        val channel = sseStream(
            "message_start" to messageStartData,
            "content_block_delta" to deltaData1,
            "content_block_delta" to deltaData2,
            "message_stop" to messageStopData,
        )
        val result = processSseChannel(channel, "fallback-model")
        assertIs<Result.Success<*>>(result)
        val response = (result as Result.Success).data!!
        assertEquals("claude-sonnet-5", response.model)
        assertEquals("Hello, world", response.content)
    }

    // Case 1: connection drops before MessageStop → Failure with meaningful message
    @Test
    fun connectionDropBeforeMessageStopEmitsFailure() = runTest {
        val channel = sseStream(
            "message_start" to messageStartData,
            "content_block_delta" to deltaData1,
            // no message_stop
        )
        val result = processSseChannel(channel, "fallback-model")
        assertIs<Result.Failure>(result)
        assertTrue(
            result.errorMessage?.contains("Connection closed") == true,
            "Expected 'Connection closed' in error message, got: ${result.errorMessage}"
        )
    }

    // Case 2: eventType does not leak between consecutive SSE blocks
    @Test
    fun eventTypeDoesNotLeakBetweenBlocks() = runTest {
        val channel = sseStream(
            "message_start" to messageStartData,
            "content_block_delta" to deltaData1,
            "content_block_delta" to deltaData2,
            "message_stop" to messageStopData,
        )
        val result = processSseChannel(channel, "fallback-model")
        // If eventType leaked, content_block_delta blocks might be parsed as message_start
        // causing detectedModel to stay "fallback-model" and content to be empty.
        // Correct behavior: model from message_start, content accumulated from deltas.
        assertIs<Result.Success<*>>(result)
        val response = (result as Result.Success).data!!
        assertEquals("claude-sonnet-5", response.model, "Model should come from message_start, not leak")
        assertEquals("Hello, world", response.content, "Content should be accumulated from both deltas")
    }

    // Case 2: unknown event types between valid events are silently ignored
    @Test
    fun unknownEventsAreIgnored() = runTest {
        val channel = sseStream(
            "message_start" to messageStartData,
            "ping" to "{}",
            "content_block_start" to """{"type":"content_block_start","index":0,"content_block":{"type":"text","text":""}}""",
            "content_block_delta" to deltaData1,
            "message_stop" to messageStopData,
        )
        val result = processSseChannel(channel, "fallback-model")
        assertIs<Result.Success<*>>(result)
        assertEquals("Hello", (result as Result.Success).data!!.content)
    }

    // Case 1: empty stream (no events at all) → Failure
    @Test
    fun emptyStreamEmitsFailure() = runTest {
        val channel = ByteReadChannel(ByteArray(0))
        val result = processSseChannel(channel, "fallback-model")
        assertIs<Result.Failure>(result)
    }
}
