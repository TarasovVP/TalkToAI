package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.AuthService
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.auth.request.ChangePasswordBody
import com.vnteam.talktoai.data.network.auth.request.ResetPasswordBody
import com.vnteam.talktoai.data.network.auth.response.ChangePasswordResponse
import com.vnteam.talktoai.data.network.auth.response.ResetPasswordResponse
import com.vnteam.talktoai.data.network.auth.response.SignInAnonymouslyResponse
import com.vnteam.talktoai.data.network.auth.response.SignInEmailResponse
import com.vnteam.talktoai.data.network.auth.response.SignUpEmailResponse
import com.vnteam.talktoai.data.network.handleResponse
import com.vnteam.talktoai.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImpl(private val authService: AuthService) :
    AuthRepository {

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
        // TODO implement id token
        val httpResponse = authService.changePassword(changePasswordBody)
        val changePasswordResponse = httpResponse.handleResponse<ChangePasswordResponse>()
        println("authTAG changePassword changePasswordResponse: $changePasswordResponse")
        return changePasswordResponse
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

    override fun fetchSignInMethodsForEmail(email: String): Flow<List<String>> =
        callbackFlow {
            /*firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.invoke(Result.Success(task.result?.signInMethods))
                    }
                }.addOnFailureListener { exception ->
                    result.invoke(Result.Failure(exception.localizedMessage))
                }*/
        }



    override fun signInWithGoogle(idToken: String): Flow<String> = callbackFlow {
        /*val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }



    override fun reAuthenticate(/*authCredential: AuthCredential*/): Flow<Unit> = callbackFlow {
        /*firebaseAuth.currentUser?.reauthenticate(authCredential)
            ?.addOnSuccessListener {
                result.invoke(Result.Success())
            }?.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun deleteUser(): Flow<Unit> = callbackFlow {
        /*firebaseAuth.currentUser?.delete()
            ?.addOnSuccessListener {
                signOut(result)
            }?.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun signOut(): Flow<Unit> = callbackFlow {
        /*googleSignInClient.signOut().addOnSuccessListener {
            firebaseAuth.signOut()
            result.invoke(Result.Success())
        }.addOnFailureListener { exception ->
            result.invoke(Result.Failure(exception.localizedMessage))
        }*/
    }

    override fun googleSignOut() {
        // TODO implement
    }

    override fun googleSignIn() {
        // TODO implement
    }
}