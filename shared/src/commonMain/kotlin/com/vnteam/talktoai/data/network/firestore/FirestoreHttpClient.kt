package com.vnteam.talktoai.data.network.firestore

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import secrets.Secrets

class FirestoreHttpClient(json: Json) {
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(DefaultRequest) {
            url(Secrets.FIRESTORE_BASE_URL)
            contentType(ContentType.Application.Json)
        }
    }
}
