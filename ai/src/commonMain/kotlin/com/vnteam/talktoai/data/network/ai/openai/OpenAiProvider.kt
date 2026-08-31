package com.vnteam.talktoai.data.network.ai.openai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.UNKNOWN_ERROR
import com.vnteam.talktoai.data.network.ai.AiProvider
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.openai.request.ApiRequest
import com.vnteam.talktoai.data.network.ai.openai.request.MessageApi
import com.vnteam.talktoai.data.network.ai.openai.response.ApiResponse
import com.vnteam.talktoai.data.network.ai.request.Message
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OpenAiProvider(private val service: OpenAiService) : AiProvider {

    override fun sendMessage(
        model: String,
        messages: List<Message>,
        apiKey: String?,
    ): Flow<Result<AiTextResponse>> = flow {
        val apiMessages = messages.map { MessageApi(role = it.role, content = it.content) }
        val request = ApiRequest(model = model, messages = apiMessages)
        val response = try {
            service.sendRequest(request, apiKey)
        } catch (e: Exception) {
            emit(Result.Failure(e.message ?: UNKNOWN_ERROR))
            return@flow
        }
        if (response.status.value in 200..299) {
            val parsed = try {
                val apiResp = response.body<ApiResponse>()
                AiTextResponse(
                    model = apiResp.model.orEmpty(),
                    content = apiResp.choices?.firstOrNull()?.message?.content.orEmpty(),
                )
            } catch (e: Exception) {
                emit(Result.Failure(e.message ?: UNKNOWN_ERROR))
                return@flow
            }
            emit(Result.Success(parsed))
        } else {
            val rawBody = response.bodyAsText()
            emit(Result.Failure(rawBody, response.status.value))
        }
    }
}
