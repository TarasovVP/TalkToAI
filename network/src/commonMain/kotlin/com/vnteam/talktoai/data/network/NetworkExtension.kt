package com.vnteam.talktoai.data.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

const val UNKNOWN_ERROR = "Unknown error"

val errorJson = Json { ignoreUnknownKeys = true }

fun parseErrorMessage(body: String): String = try {
    val root = errorJson.parseToJsonElement(body).jsonObject
    root[NetworkConstants.ERROR_KEY]?.jsonObject
        ?.get(NetworkConstants.MESSAGE_KEY)?.jsonPrimitive?.content ?: body
} catch (e: Exception) { body }

fun isTemperatureDeprecatedError(statusCode: Int, body: String): Boolean {
    if (statusCode != 400) return false
    return try {
        val errorObj = errorJson.parseToJsonElement(body).jsonObject[NetworkConstants.ERROR_KEY]?.jsonObject
            ?: return false
        val message = errorObj[NetworkConstants.MESSAGE_KEY]?.jsonPrimitive?.content.orEmpty()
        message.contains(NetworkConstants.TEMPERATURE_KEYWORD, ignoreCase = true)
    } catch (e: Exception) { false }
}

fun isModelNotSupportedError(statusCode: Int, body: String): Boolean {
    if (statusCode != 400) return false
    return try {
        val errorObj = errorJson.parseToJsonElement(body).jsonObject[NetworkConstants.ERROR_KEY]?.jsonObject
            ?: return false
        val type = errorObj[NetworkConstants.TYPE_KEY]?.jsonPrimitive?.content.orEmpty()
        val message = errorObj[NetworkConstants.MESSAGE_KEY]?.jsonPrimitive?.content.orEmpty()
        (type == NetworkConstants.ERROR_TYPE_NOT_FOUND || type == NetworkConstants.ERROR_TYPE_INVALID_REQUEST) &&
                message.contains(NetworkConstants.MODEL_KEYWORD, ignoreCase = true)
    } catch (e: Exception) { false }
}

suspend inline fun <reified T> HttpResponse?.handleResponse(): Result<T> {
    return when {
        this == null -> Result.Failure(UNKNOWN_ERROR)
        status.value !in 200..299 -> {
            val text = bodyAsText()
            val message = try {
                val root = errorJson.parseToJsonElement(text).jsonObject
                root[NetworkConstants.ERROR_KEY]?.jsonObject?.get(NetworkConstants.MESSAGE_KEY)?.jsonPrimitive?.content ?: text
            } catch (e: Exception) {
                text
            }
            Result.Failure(message, statusCode = status.value)
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
