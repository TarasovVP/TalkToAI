package com.vnteam.talktoai.data.network.auth

import com.vnteam.talktoai.data.network.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import secrets.Secrets

class AuthHttpClient(
    json: Json,
    enableLogging: Boolean = false,
    connectTimeoutMillis: Long = 15_000,
    requestTimeoutMillis: Long = 30_000,
    socketTimeoutMillis: Long = 30_000
) {
    val getHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(DefaultRequest) {
            url(Secrets.AUTH_BASE_URL)
            url {
                parameters.append(NetworkConstants.KEY, Secrets.AUTH_API_KEY)
            }
            contentType(ContentType.Application.Json)
        }
        install(HttpTimeout) {
            this.connectTimeoutMillis = connectTimeoutMillis
            this.requestTimeoutMillis = requestTimeoutMillis
            this.socketTimeoutMillis = socketTimeoutMillis
        }
        if (enableLogging) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        val redacted = message.replace(Secrets.AUTH_API_KEY, "***")
                        println("Ktor => $redacted")
                    }
                }
                level = LogLevel.HEADERS
            }
        }
    }
}
