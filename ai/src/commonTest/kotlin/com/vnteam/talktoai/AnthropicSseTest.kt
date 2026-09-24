package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.anthropic.processSseChannel
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
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

    private suspend fun collect(channel: ByteReadChannel, initialModel: String) =
        flow { processSseChannel(channel, initialModel) }.toList()

    @Test
    fun fullStreamEmitsSuccessAsLastResult() = runTest {
        val channel = sseStream(
            "message_start" to messageStartData,
            "content_block_delta" to deltaData1,
            "content_block_delta" to deltaData2,
            "message_stop" to messageStopData,
        )
        val results = collect(channel, "fallback-model")
        val last = results.last()
        assertIs<Result.Success<AiTextResponse>>(last)
        val response = last.data!!
        assertEquals("claude-sonnet-5", response.model)
        assertEquals("Hello, world", response.content)
    }

    @Test
    fun connectionDropBeforeMessageStopEmitsFailure() = runTest {
        val channel = sseStream(
            "message_start" to messageStartData,
            "content_block_delta" to deltaData1,
            // no message_stop
        )
        val results = collect(channel, "fallback-model")
        val last = results.last()
        assertIs<Result.Failure>(last)
        assertTrue(
            last.errorMessage?.contains("Connection closed") == true,
            "Expected 'Connection closed' in error message, got: ${last.errorMessage}"
        )
    }

    @Test
    fun eventTypeDoesNotLeakBetweenBlocks() = runTest {
        val channel = sseStream(
            "message_start" to messageStartData,
            "content_block_delta" to deltaData1,
            "content_block_delta" to deltaData2,
            "message_stop" to messageStopData,
        )
        val results = collect(channel, "fallback-model")
        val last = results.last()
        assertIs<Result.Success<AiTextResponse>>(last)
        val response = last.data!!
        assertEquals("claude-sonnet-5", response.model, "Model should come from message_start, not leak")
        assertEquals("Hello, world", response.content, "Content should be accumulated from both deltas")
    }

    @Test
    fun unknownEventsAreIgnored() = runTest {
        val channel = sseStream(
            "message_start" to messageStartData,
            "ping" to "{}",
            "content_block_start" to """{"type":"content_block_start","index":0,"content_block":{"type":"text","text":""}}""",
            "content_block_delta" to deltaData1,
            "message_stop" to messageStopData,
        )
        val results = collect(channel, "fallback-model")
        val last = results.last()
        assertIs<Result.Success<AiTextResponse>>(last)
        assertEquals("Hello", last.data!!.content)
    }

    @Test
    fun emptyStreamEmitsFailure() = runTest {
        val channel = ByteReadChannel(ByteArray(0))
        val results = collect(channel, "fallback-model")
        assertIs<Result.Failure>(results.last())
    }

    @Test
    fun multipleContentBlockDeltasProduceMultipleEmits() = runTest {
        val channel = sseStream(
            "message_start" to messageStartData,
            "content_block_delta" to deltaData1,
            "content_block_delta" to deltaData2,
            "message_stop" to messageStopData,
        )
        val results = collect(channel, "fallback-model")
        val successes = results.filterIsInstance<Result.Success<AiTextResponse>>()
        assertTrue(successes.size >= 2, "Expected more than one Success emit, got ${successes.size}")
    }

    @Test
    fun manyRapidDeltasAreThrottledButFinalEmitHasFullText() = runTest {
        val deltaCount = 30
        val events = buildList {
            add("message_start" to messageStartData)
            repeat(deltaCount) { i ->
                add("content_block_delta" to """{"type":"content_block_delta","index":0,"delta":{"type":"text_delta","text":"$i "}}""")
            }
            add("message_stop" to messageStopData)
        }.toTypedArray()
        val channel = sseStream(*events)
        val results = collect(channel, "fallback-model")
        val successes = results.filterIsInstance<Result.Success<AiTextResponse>>()
        assertTrue(successes.size < deltaCount, "Expected throttling to reduce emit count below $deltaCount, got ${successes.size}")
        val expectedFullText = (0 until deltaCount).joinToString("") { "$it " }
        assertEquals(expectedFullText, successes.last().data!!.content)
    }
}
