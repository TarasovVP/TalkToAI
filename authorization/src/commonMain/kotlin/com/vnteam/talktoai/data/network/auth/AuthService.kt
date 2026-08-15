package com.vnteam.talktoai.data.network.auth

import com.vnteam.talktoai.data.network.auth.AuthConstants.Endpoints.ACCOUNT_CREATE_AUTH_URI
import com.vnteam.talktoai.data.network.auth.AuthConstants.Endpoints.ACCOUNT_DELETE
import com.vnteam.talktoai.data.network.auth.AuthConstants.Endpoints.ACCOUNT_SEND_OOB_CODE
import com.vnteam.talktoai.data.network.auth.AuthConstants.Endpoints.ACCOUNT_SIGN_IN_WITH_PASSWORD
import com.vnteam.talktoai.data.network.auth.AuthConstants.Endpoints.ACCOUNT_SIGN_UP
import com.vnteam.talktoai.data.network.auth.AuthConstants.Endpoints.ACCOUNT_UPDATE
import com.vnteam.talktoai.data.network.auth.AuthConstants.Endpoints.SECURE_TOKEN_URL
import com.vnteam.talktoai.data.network.auth.AuthConstants.OAuth.GRANT_TYPE
import com.vnteam.talktoai.data.network.auth.AuthConstants.OAuth.GRANT_TYPE_REFRESH_TOKEN
import com.vnteam.talktoai.data.network.auth.AuthConstants.OAuth.REFRESH_TOKEN_KEY
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.auth.request.ChangePasswordBody
import com.vnteam.talktoai.data.network.auth.request.DeleteAccountBody
import com.vnteam.talktoai.data.network.auth.request.ProvidersForEmailBody
import com.vnteam.talktoai.data.network.auth.request.ResetPasswordBody
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.parameters

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

    suspend fun exchangeRefreshToken(refreshToken: String): HttpResponse? {
        return try {
            authHttpClient.getHttpClient.post(SECURE_TOKEN_URL) {
                setBody(FormDataContent(parameters {
                    append(GRANT_TYPE, GRANT_TYPE_REFRESH_TOKEN)
                    append(REFRESH_TOKEN_KEY, refreshToken)
                }))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
