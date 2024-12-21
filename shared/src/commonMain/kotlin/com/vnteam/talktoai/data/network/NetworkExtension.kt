package com.vnteam.talktoai.data.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

const val CONNECTION_EXCEPTION = "Connection Exception. Check if the server is running."
const val UNKNOWN_ERROR = "Unknown error"

suspend inline fun <reified T> HttpResponse?.handleResponse(): NetworkResult<T> {
    return when {
        this == null -> NetworkResult.Failure(UNKNOWN_ERROR)
        status.value in 400..405 -> {
            println("Error ${bodyAsText()}")
            NetworkResult.Failure(CONNECTION_EXCEPTION)
        }

        status.value !in 200..299 -> {
            val error = bodyAsText()
            NetworkResult.Failure(error)
        }

        else -> {
            try {
                val result = body<T>()
                NetworkResult.Success(result)
            } catch (e: Exception) {
                NetworkResult.Failure(e.message ?: UNKNOWN_ERROR)
            }
        }
    }
}
