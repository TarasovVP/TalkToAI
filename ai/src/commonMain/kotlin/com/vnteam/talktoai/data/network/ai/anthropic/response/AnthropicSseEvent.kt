package com.vnteam.talktoai.data.network.ai.anthropic.response

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

sealed class AnthropicSseEvent {
    data class MessageStart(val model: String) : AnthropicSseEvent()
    data class ContentBlockDelta(val text: String) : AnthropicSseEvent()
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
                AnthropicSseEvent.MessageStart(model)
            }
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
