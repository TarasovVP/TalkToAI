package com.vnteam.talktoai.data.network.ai.anthropic

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.UNKNOWN_ERROR
import com.vnteam.talktoai.data.network.ai.AiProvider
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.anthropic.response.AnthropicSseEvent
import com.vnteam.talktoai.data.network.ai.anthropic.response.parseAnthropicSseEvent
import com.vnteam.talktoai.data.network.ai.openai.request.MessageApi
import com.vnteam.talktoai.data.network.ai.request.Message
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnthropicProvider(private val service: AnthropicService) : AiProvider {

    override fun sendMessage(
        model: String,
        messages: List<Message>,
        apiKey: String?,
    ): Flow<Result<AiTextResponse>> = flow {
        val apiMessages = messages.map { MessageApi(role = it.role, content = it.content) }
        val request = apiMessages.toAnthropicRequest(model)
        val response = try {
            service.sendMessage(request, apiKey)
        } catch (e: Exception) {
            emit(Result.Failure(e.message ?: UNKNOWN_ERROR))
            return@flow
        }
        if (response.status.value !in 200..299) {
            emit(Result.Failure(response.bodyAsText(), response.status.value))
            return@flow
        }
        emit(processSseChannel(response.bodyAsChannel(), model))
    }
}

internal suspend fun processSseChannel(
    channel: ByteReadChannel,
    initialModel: String,
): Result<AiTextResponse> {
    val contentBuffer = StringBuilder()
    var detectedModel = initialModel
    var eventType = ""
    var receivedMessageStop = false
    while (!channel.isClosedForRead) {
        val line = channel.readUTF8Line() ?: break
        when {
            line.startsWith("event:") -> eventType = line.removePrefix("event:").trim()
            line.startsWith("data:") -> {
                val data = line.removePrefix("data:").trim()
                when (val event = parseAnthropicSseEvent(eventType, data)) {
                    is AnthropicSseEvent.MessageStart -> detectedModel = event.model
                    is AnthropicSseEvent.ContentBlockDelta -> contentBuffer.append(event.text)
                    is AnthropicSseEvent.MessageStop -> receivedMessageStop = true
                    else -> Unit
                }
            }
        }
    }
    return if (receivedMessageStop) {
        Result.Success(AiTextResponse(model = detectedModel, content = contentBuffer.toString()))
    } else {
        Result.Failure("Connection closed before response was complete")
    }
}
