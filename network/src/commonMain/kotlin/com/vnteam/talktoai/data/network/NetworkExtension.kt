package com.vnteam.talktoai.data.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

const val UNKNOWN_ERROR = "Unknown error"

val errorJson = Json { ignoreUnknownKeys = true }

suspend inline fun <reified T> HttpResponse?.handleResponse(): Result<T> {
    return when {
        this == null -> Result.Failure(UNKNOWN_ERROR)
        status.value !in 200..299 -> {
            val text = bodyAsText()
            println("Error $text")
            val message = try {
                val root = errorJson.parseToJsonElement(text).jsonObject
                root["error"]?.jsonObject?.get("message")?.jsonPrimitive?.content ?: text
            } catch (e: Exception) {
                text
            }
            Result.Failure(message)
        }
        else -> {
            try {
                val result = body<T>()
                Result.Success(result)
            } catch (e: Exception) {
                Result.Failure(e.message ?: UNKNOWN_ERROR)
            }
        }
    }
}
