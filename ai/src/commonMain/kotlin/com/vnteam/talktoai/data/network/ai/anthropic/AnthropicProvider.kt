package com.vnteam.talktoai.data.network.ai.anthropic

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.UNKNOWN_ERROR
import com.vnteam.talktoai.data.network.ai.AiProvider
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.EmitThrottler
import com.vnteam.talktoai.data.network.ai.TokenUsage
import com.vnteam.talktoai.data.network.ai.anthropic.response.AnthropicSseEvent
import com.vnteam.talktoai.data.network.ai.anthropic.response.parseAnthropicSseEvent
import com.vnteam.talktoai.data.network.ai.request.Message
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class AnthropicProvider(private val service: AnthropicService) : AiProvider {

    override fun sendMessage(
        model: String,
        messages: List<Message>,
        apiKey: String?,
        temperature: Float?,
    ): Flow<Result<AiTextResponse>> = flow {
        val request = messages.toAnthropicRequest(model, temperature)
        try {
            service.sendMessage(request, apiKey) { response ->
                if (response.status.value !in 200..299) {
                    emit(Result.Failure(response.bodyAsText(), response.status.value))
                    return@sendMessage
                }
                processSseChannel(response.bodyAsChannel(), model)
            }
        } catch (e: Exception) {
            emit(Result.Failure(e.message ?: UNKNOWN_ERROR))
        }
    }
}

internal suspend fun FlowCollector<Result<AiTextResponse>>.processSseChannel(
    channel: ByteReadChannel,
    initialModel: String,
) {
    val contentBuffer = StringBuilder()
    var detectedModel = initialModel
    var eventType = ""
    var receivedMessageStop = false
    var startUsage: TokenUsage? = null
    var finalUsage: TokenUsage? = null
    val throttler = EmitThrottler()
    while (!channel.isClosedForRead) {
        val line = channel.readUTF8Line() ?: break
        when {
            line.startsWith("event:") -> eventType = line.removePrefix("event:").trim()
            line.startsWith("data:") -> {
                val data = line.removePrefix("data:").trim()
                when (val event = parseAnthropicSseEvent(eventType, data)) {
                    is AnthropicSseEvent.MessageStart -> {
                        detectedModel = event.model
                        startUsage = event.usage
                    }

                    is AnthropicSseEvent.MessageDelta -> finalUsage = event.usage

                    is AnthropicSseEvent.ContentBlockDelta -> {
                        contentBuffer.append(event.text)
                        if (throttler.shouldEmit()) {
                            emit(Result.Success(AiTextResponse(model = detectedModel, content = contentBuffer.toString())))
                            throttler.markEmitted()
                        }
                    }

                    is AnthropicSseEvent.MessageStop -> receivedMessageStop = true
                    else -> Unit
                }
            }
        }
        if (receivedMessageStop) break
    }
    emit(Result.Success(AiTextResponse(model = detectedModel, content = contentBuffer.toString(), usage = finalUsage ?: startUsage)))
    if (!receivedMessageStop) {
        emit(Result.Failure("Connection closed before response was complete"))
    }
}
