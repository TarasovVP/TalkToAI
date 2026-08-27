package com.vnteam.talktoai.data.network.ai.openai

import com.vnteam.talktoai.data.network.NetworkConstants
import com.vnteam.talktoai.data.network.ai.openai.request.ApiRequest
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class OpenAiService(
    private val openAiHttpClient: OpenAiHttpClient,
) {
    suspend fun sendRequest(apiRequest: ApiRequest, apiKey: String? = null): HttpResponse {
        return openAiHttpClient.httpClient.post(CHAT_COMPLETION) {
            if (!apiKey.isNullOrEmpty()) {
                headers[NetworkConstants.OPENAI_AUTHORIZATION_HEADER] = "Bearer $apiKey"
            }
            setBody(apiRequest)
        }
    }

    suspend fun getModels(apiKey: String? = null): HttpResponse {
        return openAiHttpClient.httpClient.get(MODELS) {
            if (!apiKey.isNullOrEmpty()) {
                headers[NetworkConstants.OPENAI_AUTHORIZATION_HEADER] = "Bearer $apiKey"
            }
        }
    }
}

private const val CHAT_COMPLETION = "/v1/chat/completions"
private const val MODELS = "/v1/models"
