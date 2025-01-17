package com.vnteam.talktoai.data.network.auth

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse

class AuthService(
    private val baseUrl: String,
    private val httpClient: HttpClient,
) {
    suspend fun signInWithEmailAndPassword(email: String, password: String): HttpResponse? {
        val httpResponse = try {
            httpClient.post("${baseUrl}accounts:signInWithPassword") {
                mapOf(
                    "email" to email,
                    "password" to password,
                    "returnSecureToken" to true
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }

    suspend fun signInAnonymously(): HttpResponse? {
        val httpResponse = try {
            httpClient.post("${baseUrl}accounts:signInAnonymously") {
                mapOf(
                    "returnSecureToken" to true
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }

    suspend fun resetPassword(email: String): HttpResponse? {
        val httpResponse = try {
            httpClient.post("${baseUrl}accounts:resetPassword") {
                mapOf(
                    "email" to email,
                    "returnSecureToken" to true
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }
}