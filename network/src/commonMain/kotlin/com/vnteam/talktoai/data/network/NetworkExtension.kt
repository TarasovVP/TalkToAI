package com.vnteam.talktoai.data.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

const val CONNECTION_EXCEPTION = "Connection Exception. Check if the server is running."
const val UNKNOWN_ERROR = "Unknown error"

suspend inline fun <reified T> HttpResponse?.handleResponse(): Result<T> {
    return when {
        this == null -> Result.Failure(UNKNOWN_ERROR)
        status.value in 400..405 -> {
            println("Error ${bodyAsText()}")
            Result.Failure(CONNECTION_EXCEPTION)
        }

        status.value !in 200..299 -> {
            val error = bodyAsText()
            Result.Failure(error)
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
