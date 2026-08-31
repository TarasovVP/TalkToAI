package com.vnteam.talktoai.data.network.ai.openai

import com.vnteam.talktoai.data.network.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import com.vnteam.talktoai.data.network.ai.isDebug
import com.vnteam.talktoai.data.network.ai.platformLogger
import kotlinx.serialization.json.Json
import secrets.Secrets

class OpenAiHttpClient(json: Json) {
    internal val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(DefaultRequest) {
            url(Secrets.OPENAI_BASE_URL)
            contentType(ContentType.Application.Json)
            header(NetworkConstants.OPENAI_AUTHORIZATION_HEADER, "${NetworkConstants.BEARER_PREFIX}${Secrets.OPENAI_API_KEY}")
            header(NetworkConstants.OPENAI_ORGANIZATION_HEADER, Secrets.ORGANIZATION_ID)
            header(NetworkConstants.OPENAI_PROJECT_HEADER, Secrets.PROJECT_ID)
        }
        install(Logging) {
            logger = platformLogger()
            level = if (isDebug) LogLevel.ALL else LogLevel.NONE
        }
    }
}