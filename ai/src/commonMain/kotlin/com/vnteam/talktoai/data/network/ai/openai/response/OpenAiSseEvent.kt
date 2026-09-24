package com.vnteam.talktoai.data.network.ai.openai.response

import kotlinx.serialization.json.Json
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

    data object Done : OpenAiSseEvent()
    data object Unknown : OpenAiSseEvent()
}

fun parseOpenAiSseEvent(data: String): OpenAiSseEvent {
    if (data == "[DONE]") return OpenAiSseEvent.Done
    return try {
        val root = Json.parseToJsonElement(data).jsonObject
        val model = root["model"]?.jsonPrimitive?.contentOrNull
        val choice = root["choices"]?.jsonArray?.firstOrNull()?.jsonObject
        val delta = choice?.get("delta")?.jsonObject
        val contentDelta = delta?.get("content")?.jsonPrimitive?.contentOrNull
        val finishReason = choice?.get("finish_reason")?.jsonPrimitive?.contentOrNull
        OpenAiSseEvent.Chunk(model = model, contentDelta = contentDelta, finishReason = finishReason)
    } catch (e: Exception) {
        OpenAiSseEvent.Unknown
    }
}
