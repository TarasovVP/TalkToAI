package com.vnteam.talktoai.data.network.ai.openai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.UNKNOWN_ERROR
import com.vnteam.talktoai.data.network.ai.AiProvider
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.EmitThrottler
import com.vnteam.talktoai.data.network.ai.TokenUsage
import com.vnteam.talktoai.data.network.ai.openai.response.OpenAiSseEvent
import com.vnteam.talktoai.data.network.ai.openai.response.parseOpenAiSseEvent
import com.vnteam.talktoai.data.network.ai.request.Message
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class OpenAiProvider(private val service: OpenAiService) : AiProvider {

    override fun sendMessage(
        model: String,
        messages: List<Message>,
        apiKey: String?,
        temperature: Float?,
    ): Flow<Result<AiTextResponse>> = flow {
        val request = messages.toOpenAiRequest(model, temperature)
        try {
            service.sendRequest(request, apiKey) { response ->
                if (response.status.value !in 200..299) {
                    emit(Result.Failure(response.bodyAsText(), response.status.value))
                    return@sendRequest
                }
                processOpenAiSseChannel(response.bodyAsChannel(), model)
            }
        } catch (e: Exception) {
            emit(Result.Failure(e.message ?: UNKNOWN_ERROR))
        }
    }
}

internal suspend fun FlowCollector<Result<AiTextResponse>>.processOpenAiSseChannel(
    channel: ByteReadChannel,
    initialModel: String,
) {
    val contentBuffer = StringBuilder()
    var detectedModel = initialModel
    var finished = false
    var sawFinishReason = false
    var usage: TokenUsage? = null
    val throttler = EmitThrottler()
    while (!channel.isClosedForRead) {
        val line = channel.readUTF8Line() ?: break
        if (!line.startsWith("data:")) continue
        val data = line.removePrefix("data:").trim()
        when (val event = parseOpenAiSseEvent(data)) {
            is OpenAiSseEvent.Done -> finished = true
            is OpenAiSseEvent.Usage -> {
                usage = event.usage
                finished = true
            }

            is OpenAiSseEvent.Chunk -> {
                event.model?.let { detectedModel = it }
                event.contentDelta?.let { delta ->
                    if (delta.isNotEmpty()) contentBuffer.append(delta)
                    if (throttler.shouldEmit()) {
                        emit(Result.Success(AiTextResponse(model = detectedModel, content = contentBuffer.toString())))
                        throttler.markEmitted()
                    }
                }
                if (event.finishReason != null) sawFinishReason = true
            }

            OpenAiSseEvent.Unknown -> Unit
        }
        if (finished) break
    }
    emit(Result.Success(AiTextResponse(model = detectedModel, content = contentBuffer.toString(), usage = usage)))
    if (!finished && !sawFinishReason) {
        emit(Result.Failure("Connection closed before response was complete"))
    }
}
