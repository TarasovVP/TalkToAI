package com.vnteam.talktoai.data.network.ai.anthropic

import com.vnteam.talktoai.data.network.NetworkConstants
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class AnthropicService(
    private val anthropicHttpClient: AnthropicHttpClient,
) {
    suspend fun sendMessage(request: AnthropicRequest, apiKey: String? = null): HttpResponse {
        return anthropicHttpClient.httpClient.post(MESSAGES) {
            if (!apiKey.isNullOrEmpty()) {
                headers[NetworkConstants.ANTHROPIC_API_KEY_HEADER] = apiKey
            }
            setBody(request)
        }
    }
}

private const val MESSAGES = "/v1/messages"
