package com.vnteam.talktoai.data.network.ai.openai

import com.vnteam.talktoai.data.network.NetworkConstants
import com.vnteam.talktoai.data.network.ai.openai.request.ApiRequest
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class OpenAiService(
    private val openAiHttpClient: OpenAiHttpClient,
) {
    suspend fun <T> sendRequest(
        apiRequest: ApiRequest,
        apiKey: String? = null,
        block: suspend (HttpResponse) -> T,
    ): T {
        return openAiHttpClient.httpClient.preparePost(CHAT_COMPLETION) {
            if (!apiKey.isNullOrEmpty()) {
                headers[NetworkConstants.OPENAI_AUTHORIZATION_HEADER] = "${NetworkConstants.BEARER_PREFIX}$apiKey"
            }
            setBody(apiRequest)
        }.execute(block)
    }
}

private const val CHAT_COMPLETION = "/v1/chat/completions"
