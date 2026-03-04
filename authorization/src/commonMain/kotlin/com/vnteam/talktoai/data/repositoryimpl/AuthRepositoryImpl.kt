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
import com.vnteam.talktoai.data.sdk.GoogleAuthHandler
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
    private val googleAuthHandler: GoogleAuthHandler,
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

    override suspend fun googleSignIn(idToken: String): Result<Unit> {
        return try {
            logger?.debug(TAG, "Google sign in with provided token")
            // TODO: Send idToken to backend for verification
            // val result = authService.googleSignIn(GoogleSignInRequest(idToken))
            // return executeAuthRequest("googleSignIn") { result }

            // For now using local handler
            googleAuthHandler.signIn()
            Result.Success(Unit)
        } catch (e: Exception) {
            logger?.error(TAG, "Google sign in failed", e)
            Result.Failure("Google sign in failed: ${e.message}")
        }
    }

    override suspend fun googleSignOut(): Result<Unit> {
        return try {
            logger?.debug(TAG, "Google sign out")
            googleAuthHandler.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            logger?.error(TAG, "Google sign out failed", e)
            Result.Failure("Google sign out failed: ${e.message}")
        }
    }

    override fun addAuthStateListener() {
        // TODO: Implement when needed
        logger?.debug(TAG, "addAuthStateListener not implemented")
    }

    override fun removeAuthStateListener() {
        // TODO: Implement when needed
        logger?.debug(TAG, "removeAuthStateListener not implemented")
    }

    override fun isGoogleAuthUser(): Boolean {
        return try {
            val token = googleAuthHandler.getToken()
            val isGoogleUser = !token.isNullOrEmpty()
            logger?.debug(TAG, "isGoogleAuthUser: $isGoogleUser")
            isGoogleUser
        } catch (e: Exception) {
            logger?.error(TAG, "Error checking Google auth user", e)
            false
        }
    }

    override fun reAuthenticate(): Flow<Unit> = flow {
        logger?.debug(TAG, "reAuthenticate called")
        throw UnsupportedOperationException("reAuthenticate is not yet implemented")
    }
}