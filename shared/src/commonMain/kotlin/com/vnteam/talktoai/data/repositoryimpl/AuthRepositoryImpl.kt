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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val googleAuthHandler: GoogleAuthHandler,
) :
    AuthRepository {

    override suspend fun fetchProvidersForEmail(providersForEmailBody: ProvidersForEmailBody): Result<ProvidersForEmailResponse> {
        val httpResponse = authService.fetchProvidersForEmail(providersForEmailBody)
        val providersForEmailResponse = httpResponse.handleResponse<ProvidersForEmailResponse>()
        println("authTAG fetchProvidersForEmail providersForEmailResponse: $providersForEmailResponse")
        return providersForEmailResponse
    }

    override suspend fun signInWithEmailAndPassword(authBody: AuthBody): Result<SignInEmailResponse> {
        val httpResponse = authService.signInWithEmailAndPassword(authBody)
        val signInEmailResponse = httpResponse.handleResponse<SignInEmailResponse>()
        println("authTAG signInWithEmailAndPassword signInEmailResponse: $signInEmailResponse")
        return signInEmailResponse
    }

    override suspend fun signInAnonymously(authBody: AuthBody): Result<SignInAnonymouslyResponse> {
        val httpResponse = authService.signInAnonymously(authBody)
        val signInAnonymouslyResponse = httpResponse.handleResponse<SignInAnonymouslyResponse>()
        println("authTAG signInAnonymously signInAnonymouslyResponse: $signInAnonymouslyResponse")
        return signInAnonymouslyResponse
    }

    override suspend fun resetPassword(resetPasswordBody: ResetPasswordBody): Result<ResetPasswordResponse> {
        val httpResponse = authService.resetPassword(resetPasswordBody)
        val resetPasswordResponse = httpResponse.handleResponse<ResetPasswordResponse>()
        println("authTAG resetPassword resetPasswordResponse: $resetPasswordResponse")
        return resetPasswordResponse
    }

    override suspend fun createUserWithEmailAndPassword(authBody: AuthBody): Result<SignUpEmailResponse> {
        val httpResponse = authService.createUserWithEmailAndPassword(authBody)
        val signUpEmailResponse = httpResponse.handleResponse<SignUpEmailResponse>()
        println("authTAG createUserWithEmailAndPassword signUpEmailResponse: $signUpEmailResponse")
        return signUpEmailResponse
    }

    override suspend fun changePassword(changePasswordBody: ChangePasswordBody): Result<ChangePasswordResponse> {
        val httpResponse = authService.changePassword(changePasswordBody)
        val changePasswordResponse = httpResponse.handleResponse<ChangePasswordResponse>()
        println("authTAG changePassword changePasswordResponse: $changePasswordResponse")
        return changePasswordResponse
    }

    override suspend fun deleteUser(deleteAccountBody: DeleteAccountBody): Result<Unit> {
        val httpResponse = authService.deleteAccount(deleteAccountBody)
        val deleteAccountResponse = httpResponse.handleResponse<Unit>()
        println("authTAG deleteUser deleteAccountResponse: $deleteAccountResponse")
        return deleteAccountResponse
    }

    override suspend fun googleSignIn(idToken: String): Result<Unit> {
        googleAuthHandler.signIn()
        return Result.Success(Unit)
    }

    override suspend fun googleSignOut(): Result<Unit> {
        googleAuthHandler.signOut()
        return Result.Success(Unit)
    }


    override fun addAuthStateListener(/*authStateListener: AuthStateListener*/) {
        //firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun removeAuthStateListener(/*authStateListener: AuthStateListener*/) {
        //firebaseAuth.removeAuthStateListener(authStateListener)
    }

    override fun isGoogleAuthUser(): Boolean {
        /*firebaseAuth.currentUser?.providerData?.forEach {
            if (it.providerId == GoogleAuthProvider.PROVIDER_ID) return true
        }
        return false*/
        return false
    }


    override fun reAuthenticate(/*authCredential: AuthCredential*/): Flow<Unit> = callbackFlow {
        /*firebaseAuth.currentUser?.reauthenticate(authCredential)
            ?.addOnSuccessListener {
                result.invoke(Result.Success())
            }?.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }
}