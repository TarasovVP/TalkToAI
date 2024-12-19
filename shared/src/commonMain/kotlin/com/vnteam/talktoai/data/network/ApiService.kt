package com.vnteam.talktoai.data.network

import com.vnteam.talktoai.data.network.request.ApiRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService(
    private val baseUrl: String,
    private val httpClient: HttpClient,
) {
    suspend fun sendRequest(apiRequest: ApiRequest): HttpResponse? {
        val httpResponse = try {
            httpClient.post("${baseUrl}chat/completions") {
                contentType(ContentType.Application.Json)
                setBody(apiRequest)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }
}