package com.vnstudio.talktoai.data.network

import com.vnstudio.talktoai.domain.ApiRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

import javax.inject.Inject

class ApiService @Inject constructor(
    private val baseUrl: String,
    private val httpClient: HttpClient
) {

    suspend fun sendRequest(apiRequest: ApiRequest) = httpClient.post("$baseUrl/chat/completions") {
        setBody(apiRequest)
    }
}