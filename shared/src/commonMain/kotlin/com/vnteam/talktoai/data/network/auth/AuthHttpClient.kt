package com.vnteam.talktoai.data.network.auth

import com.vnteam.talktoai.data.network.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import kotlinx.serialization.json.Json
import secrets.Secrets

class AuthHttpClient(json: Json) {
    val getHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        defaultRequest {
            url.parameters.append(NetworkConstants.KEY, Secrets.AUTH_API_KEY)
        }
        install(DefaultRequest) {
            header(NetworkConstants.CONTENT_TYPE, "application/json")
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("Logger Ktor => $message")
                }
            }
            level = LogLevel.ALL
        }
    }
}