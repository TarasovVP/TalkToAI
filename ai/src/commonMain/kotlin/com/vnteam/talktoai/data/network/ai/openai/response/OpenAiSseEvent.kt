package com.vnteam.talktoai.data.network.ai.openai.response

import com.vnteam.talktoai.data.network.ai.TokenUsage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

sealed class OpenAiSseEvent {
    data class Chunk(
        val model: String?,
        val contentDelta: String?,
        val finishReason: String?,
    ) : OpenAiSseEvent()

    data class Usage(val usage: TokenUsage) : OpenAiSseEvent()

    data object Done : OpenAiSseEvent()
    data object Unknown : OpenAiSseEvent()
}

fun parseOpenAiSseEvent(data: String): OpenAiSseEvent {
    if (data == "[DONE]") return OpenAiSseEvent.Done
    return try {
        val root = Json.parseToJsonElement(data).jsonObject
        val choices = root["choices"]?.jsonArray
        val usageObj = root["usage"]?.takeIf { it !is JsonNull }?.jsonObject
        if (usageObj != null && (choices == null || choices.isEmpty())) {
            return usageObj.toTokenUsage()?.let { OpenAiSseEvent.Usage(it) } ?: OpenAiSseEvent.Unknown
        }
        val model = root["model"]?.jsonPrimitive?.contentOrNull
        val choice = choices?.firstOrNull()?.jsonObject
        val delta = choice?.get("delta")?.jsonObject
        val contentDelta = delta?.get("content")?.jsonPrimitive?.contentOrNull
        val finishReason = choice?.get("finish_reason")?.jsonPrimitive?.contentOrNull
        OpenAiSseEvent.Chunk(model = model, contentDelta = contentDelta, finishReason = finishReason)
    } catch (e: Exception) {
        OpenAiSseEvent.Unknown
    }
}

private fun JsonObject.toTokenUsage(): TokenUsage? {
    val input = this["prompt_tokens"]?.jsonPrimitive?.intOrNull ?: return null
    val output = this["completion_tokens"]?.jsonPrimitive?.intOrNull ?: return null
    val details = this["prompt_tokens_details"]?.takeIf { it !is JsonNull }?.jsonObject
    return TokenUsage(
        inputTokens = input,
        outputTokens = output,
        cacheReadTokens = details?.get("cached_tokens")?.jsonPrimitive?.intOrNull ?: 0,
        cacheWriteTokens = details?.get("cache_write_tokens")?.jsonPrimitive?.intOrNull ?: 0,
    )
}
