package com.vnteam.talktoai.data.network.ai

import com.vnteam.talktoai.data.network.NetworkConstants
import com.vnteam.talktoai.secrets.Config
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class AIHttpClient(json: Json) {
    val getHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(DefaultRequest) {
            header(NetworkConstants.AUTHORIZATION, "Bearer $Config.AI_API_KEY")
            header(NetworkConstants.OPENAI_ORGANIZATION, Config.ORGANIZATION_ID)
            header(NetworkConstants.OPENAI_PROJECT, Config.PROJECT_ID)
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