package com.vnteam.talktoai.data.network.auth

import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.auth.request.ChangePasswordBody
import com.vnteam.talktoai.data.network.auth.request.DeleteAccountBody
import com.vnteam.talktoai.data.network.auth.request.ProvidersForEmailBody
import com.vnteam.talktoai.data.network.auth.request.ResetPasswordBody
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class AuthService(
    private val authHttpClient: AuthHttpClient,
) {
    suspend fun fetchProvidersForEmail(providersForEmailBody: ProvidersForEmailBody): HttpResponse? {
        val httpResponse = try {
            authHttpClient.getHttpClient
                .post(ACCOUNT_CREATE_AUTH_URI) {
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
                .post(ACCOUNT_SIGN_IN_WITH_PASSWORD) {
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
            authHttpClient.getHttpClient.post(ACCOUNT_SIGN_UP) {
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
            authHttpClient.getHttpClient.post(ACCOUNT_SEND_OOB_CODE) {
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
                .post(ACCOUNT_SIGN_UP) {
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
                .post(ACCOUNT_UPDATE) {
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
                .post(ACCOUNT_DELETE) {
                    setBody(deleteAccountBody)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return httpResponse
    }
}

private const val ACCOUNT_CREATE_AUTH_URI = "/v1/accounts:createAuthUri"
private const val ACCOUNT_SIGN_IN_WITH_PASSWORD = "/v1/accounts:signInWithPassword"
private const val ACCOUNT_SEND_OOB_CODE = "/v1/accounts:sendOobCode"
private const val ACCOUNT_SIGN_UP = "/v1/accounts:signUp"
private const val ACCOUNT_UPDATE = "/v1/accounts:update"
private const val ACCOUNT_DELETE = "/v1/accounts:delete"