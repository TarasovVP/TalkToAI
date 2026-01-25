package com.vnteam.talktoai.data.network.auth

import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.auth.request.ChangePasswordBody
import com.vnteam.talktoai.data.network.auth.request.DeleteAccountBody
import com.vnteam.talktoai.data.network.auth.request.ProvidersForEmailBody
import com.vnteam.talktoai.data.network.auth.request.ResetPasswordBody
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import secrets.Secrets

class AuthService(
    private val authHttpClient: AuthHttpClient,
) {
    suspend fun fetchProvidersForEmail(providersForEmailBody: ProvidersForEmailBody): HttpResponse? {
        val httpResponse = try {
            authHttpClient.getHttpClient
                .post("${Secrets.AUTH_BASE_URL}$ACCOUNT_CREATE_AUTH_URI") {
                    setBody(providersForEmailBody)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }

    suspend fun signInWithEmailAndPassword(authBody: AuthBody): HttpResponse? {
        val httpResponse = try {
            authHttpClient.getHttpClient
                .post("${Secrets.AUTH_BASE_URL}$ACCOUNT_SIGN_IN_WITH_PASSWORD") {
                    setBody(authBody)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }

    suspend fun signInAnonymously(authBody: AuthBody): HttpResponse? {
        val httpResponse = try {
            authHttpClient.getHttpClient.post("${Secrets.AUTH_BASE_URL}$ACCOUNT_SIGN_UP") {
                setBody(authBody)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }

    suspend fun resetPassword(resetPasswordBody: ResetPasswordBody): HttpResponse? {
        println("authTAG resetPassword resetPasswordBody: $resetPasswordBody")
        val httpResponse = try {
            authHttpClient.getHttpClient.post("${Secrets.AUTH_BASE_URL}$ACCOUNT_SEND_OOB_CODE") {
                setBody(resetPasswordBody)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }

    suspend fun createUserWithEmailAndPassword(authBody: AuthBody): HttpResponse? {
        val httpResponse = try {
            authHttpClient.getHttpClient
                .post("${Secrets.AUTH_BASE_URL}$ACCOUNT_SIGN_UP") {
                    setBody(authBody)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }

    suspend fun changePassword(changePasswordBody: ChangePasswordBody): HttpResponse? {
        val httpResponse = try {
            authHttpClient.getHttpClient
                .post("${Secrets.AUTH_BASE_URL}$ACCOUNT_UPDATE") {
                    setBody(changePasswordBody)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }

    suspend fun deleteAccount(deleteAccountBody: DeleteAccountBody): HttpResponse? {
        val httpResponse = try {
            authHttpClient.getHttpClient
                .post("${Secrets.AUTH_BASE_URL}$ACCOUNT_DELETE") {
                    setBody(deleteAccountBody)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }
}

private const val ACCOUNT_CREATE_AUTH_URI = "accounts:createAuthUri"
private const val ACCOUNT_SIGN_IN_WITH_PASSWORD = "accounts:signInWithPassword"
private const val ACCOUNT_SEND_OOB_CODE = "accounts:sendOobCode"
private const val ACCOUNT_SIGN_UP = "accounts:signUp"
private const val ACCOUNT_UPDATE = "accounts:update"
private const val ACCOUNT_DELETE = "accounts:delete"