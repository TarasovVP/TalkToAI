package com.vnteam.talktoai.data.network.ai.anthropic

import com.vnteam.talktoai.data.network.NetworkConstants
import com.vnteam.talktoai.data.network.ai.isDebug
import com.vnteam.talktoai.data.network.ai.platformLogger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import secrets.Secrets

class AnthropicHttpClient(json: Json) {
    internal val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(DefaultRequest) {
            url(BASE_URL)
            contentType(ContentType.Application.Json)
            header(NetworkConstants.ANTHROPIC_API_KEY_HEADER, Secrets.ANTHROPIC_API_KEY)
            header(NetworkConstants.ANTHROPIC_VERSION_HEADER, NetworkConstants.ANTHROPIC_VERSION)
        }
        install(Logging) {
            logger = platformLogger()
            level = if (isDebug) LogLevel.ALL else LogLevel.NONE
        }
    }

    companion object {
        private const val BASE_URL = "https://api.anthropic.com"
    }
}
