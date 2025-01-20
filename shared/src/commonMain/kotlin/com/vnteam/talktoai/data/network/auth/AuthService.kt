package com.vnteam.talktoai.data.network.auth

import com.vnteam.talktoai.secrets.Config.AUTH_API_KEY
import com.vnteam.talktoai.secrets.Config.AUTH_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse

class AuthService(
    private val httpClient: HttpClient,
) {
    suspend fun signInWithEmailAndPassword(email: String, password: String): HttpResponse? {
        val httpResponse = try {
            httpClient
                .post("${AUTH_BASE_URL}accounts:signUp?key=${AUTH_API_KEY}") {
                    header("Content-Type", "application/json")
                    parameter("email", email)
                    parameter("password", password)
                    parameter("returnSecureToken", true)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }

    suspend fun signInAnonymously(): HttpResponse? {
        val httpResponse = try {
            httpClient.post("${AUTH_BASE_URL}accounts:signInAnonymously?key=${AUTH_API_KEY}") {
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
            httpClient.post("${AUTH_BASE_URL}accounts:resetPassword?key=${AUTH_API_KEY}") {
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