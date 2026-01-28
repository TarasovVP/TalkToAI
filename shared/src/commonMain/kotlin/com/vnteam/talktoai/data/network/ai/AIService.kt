package com.vnteam.talktoai.data.network.ai

import com.vnteam.talktoai.data.network.ai.request.ApiRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class AIService(
    private val aiHttpClient: AIHttpClient,
) {
    suspend fun sendRequest(apiRequest: ApiRequest): HttpResponse? {
        val httpResponse = try {
            aiHttpClient.getHttpClient.post(CHAT_COMPLETION) {
                setBody(apiRequest)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }
}

private const val CHAT_COMPLETION = "/v1/chat/completions"