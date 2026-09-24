package com.vnteam.talktoai.data.network.ai.anthropic

import com.vnteam.talktoai.data.network.NetworkConstants
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicRequest
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class AnthropicService(
    private val anthropicHttpClient: AnthropicHttpClient,
) {
    suspend fun <T> sendMessage(
        request: AnthropicRequest,
        apiKey: String? = null,
        block: suspend (HttpResponse) -> T,
    ): T {
        return anthropicHttpClient.httpClient.preparePost(MESSAGES) {
            if (!apiKey.isNullOrEmpty()) {
                headers[NetworkConstants.ANTHROPIC_API_KEY_HEADER] = apiKey
            }
            setBody(request)
        }.execute(block)
    }
}

private const val MESSAGES = "/v1/messages"
