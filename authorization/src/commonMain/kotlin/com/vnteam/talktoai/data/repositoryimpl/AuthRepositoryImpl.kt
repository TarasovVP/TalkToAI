package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.AuthService
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.auth.request.ChangePasswordBody
import com.vnteam.talktoai.data.network.auth.request.DeleteAccountBody
import com.vnteam.talktoai.data.network.auth.request.ProvidersForEmailBody
import com.vnteam.talktoai.data.network.auth.request.ResetPasswordBody
import com.vnteam.talktoai.data.network.auth.response.ChangePasswordResponse
import com.vnteam.talktoai.data.network.auth.response.ProvidersForEmailResponse
import com.vnteam.talktoai.data.network.auth.response.ResetPasswordResponse
import com.vnteam.talktoai.data.network.auth.response.SignInAnonymouslyResponse
import com.vnteam.talktoai.data.network.auth.response.SignInEmailResponse
import com.vnteam.talktoai.data.network.auth.response.SignUpEmailResponse
import com.vnteam.talktoai.data.network.handleResponse
import com.vnteam.talktoai.domain.repositories.AuthRepository
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Logger {
    fun debug(tag: String, message: String)
    fun error(tag: String, message: String, throwable: Throwable? = null)
}

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val logger: Logger? = null,
) : AuthRepository {

    companion object {
        private const val TAG = "AuthRepository"
    }

    private suspend inline fun <reified T> executeAuthRequest(
        operation: String,
        crossinline block: suspend () -> HttpResponse?
    ): Result<T> {
        return try {
            logger?.debug(TAG, "Executing $operation")
            val httpResponse = block()
            val result = httpResponse.handleResponse<T>()

            when (result) {
                is Result.Success -> logger?.debug(TAG, "$operation completed successfully")
                is Result.Failure -> logger?.error(TAG, "$operation failed: ${result.errorMessage}")
                else -> Unit
            }

            result
        } catch (e: Exception) {
            logger?.error(TAG, "$operation exception", e)
            Result.Failure("$operation failed: ${e.message}")
        }
    }

    override suspend fun fetchProvidersForEmail(
        providersForEmailBody: ProvidersForEmailBody
    ): Result<ProvidersForEmailResponse> =
        executeAuthRequest("fetchProvidersForEmail") {
            authService.fetchProvidersForEmail(providersForEmailBody)
        }

    override suspend fun signInWithEmailAndPassword(
        authBody: AuthBody
    ): Result<SignInEmailResponse> =
        executeAuthRequest("signInWithEmailAndPassword") {
            authService.signInWithEmailAndPassword(authBody)
        }

    override suspend fun signInAnonymously(
        authBody: AuthBody
    ): Result<SignInAnonymouslyResponse> =
        executeAuthRequest("signInAnonymously") {
            authService.signInAnonymously(authBody)
        }

    override suspend fun resetPassword(
        resetPasswordBody: ResetPasswordBody
    ): Result<ResetPasswordResponse> =
        executeAuthRequest("resetPassword") {
            authService.resetPassword(resetPasswordBody)
        }

    override suspend fun createUserWithEmailAndPassword(
        authBody: AuthBody
    ): Result<SignUpEmailResponse> =
        executeAuthRequest("createUserWithEmailAndPassword") {
            authService.createUserWithEmailAndPassword(authBody)
        }

    override suspend fun changePassword(
        changePasswordBody: ChangePasswordBody
    ): Result<ChangePasswordResponse> =
        executeAuthRequest("changePassword") {
            authService.changePassword(changePasswordBody)
        }

    override suspend fun deleteUser(
        deleteAccountBody: DeleteAccountBody
    ): Result<Unit> =
        executeAuthRequest("deleteUser") {
            authService.deleteAccount(deleteAccountBody)
        }

    override fun addAuthStateListener() {
        logger?.debug(TAG, "addAuthStateListener not implemented")
    }

    override fun removeAuthStateListener() {
        logger?.debug(TAG, "removeAuthStateListener not implemented")
    }

    override fun reAuthenticate(): Flow<Unit> = flow {
        logger?.debug(TAG, "reAuthenticate called")
        throw UnsupportedOperationException("reAuthenticate is not yet implemented")
    }
}
