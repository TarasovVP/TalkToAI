package com.vnteam.talktoai.data.network.ai.anthropic.response

import com.vnteam.talktoai.data.network.ai.TokenUsage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

sealed class AnthropicSseEvent {
    data class MessageStart(val model: String, val usage: TokenUsage? = null) : AnthropicSseEvent()
    data class ContentBlockDelta(val text: String) : AnthropicSseEvent()
    data class MessageDelta(val usage: TokenUsage?) : AnthropicSseEvent()
    data object MessageStop : AnthropicSseEvent()
    data object Ping : AnthropicSseEvent()
    data object Unknown : AnthropicSseEvent()
}

fun parseAnthropicSseEvent(eventType: String, data: String): AnthropicSseEvent {
    return try {
        val root = Json.parseToJsonElement(data).jsonObject
        when (eventType) {
            "message_start" -> {
                val model = root["message"]?.jsonObject?.get("model")?.jsonPrimitive?.content
                    ?: return AnthropicSseEvent.Unknown
                val usage = root["message"]?.jsonObject?.get("usage")?.jsonObject
                    ?.toTokenUsage(requireOutput = false)
                AnthropicSseEvent.MessageStart(model, usage)
            }
            "message_delta" -> AnthropicSseEvent.MessageDelta(
                root["usage"]?.jsonObject?.toTokenUsage(requireOutput = true)
            )
            "content_block_delta" -> {
                val delta = root["delta"]?.jsonObject ?: return AnthropicSseEvent.Unknown
                if (delta["type"]?.jsonPrimitive?.content == "text_delta") {
                    AnthropicSseEvent.ContentBlockDelta(delta["text"]?.jsonPrimitive?.content.orEmpty())
                } else {
                    AnthropicSseEvent.Unknown
                }
            }
            "message_stop" -> AnthropicSseEvent.MessageStop
            "ping" -> AnthropicSseEvent.Ping
            else -> AnthropicSseEvent.Unknown
        }
    } catch (e: Exception) {
        AnthropicSseEvent.Unknown
    }
}

private fun JsonObject.toTokenUsage(requireOutput: Boolean): TokenUsage? {
    val input = this["input_tokens"]?.jsonPrimitive?.intOrNull ?: return null
    val output = if (requireOutput) this["output_tokens"]?.jsonPrimitive?.intOrNull ?: return null else null
    return TokenUsage(
        inputTokens = input,
        outputTokens = output,
        cacheReadTokens = this["cache_read_input_tokens"]?.jsonPrimitive?.intOrNull ?: 0,
        cacheWriteTokens = this["cache_creation_input_tokens"]?.jsonPrimitive?.intOrNull ?: 0,
    )
}
