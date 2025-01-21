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
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(private val authService: AuthService) :
    AuthRepository {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ) = flow {
        val httpResponse = authService.signInWithEmailAndPassword(AuthBody(email, password))
        val signInEmailResponse = httpResponse.handleResponse<SignInEmailResponse>()
        println("authTAG signInWithEmailAndPassword signInEmailResponse: $signInEmailResponse")
        emit(Result.Success("signInWithEmailAndPassword"))
    }

    override suspend fun signInAnonymously() = flow {
        val httpResponse = authService.signInAnonymously(AuthBody())
        val signInAnonymouslyResponse = httpResponse.handleResponse<SignInAnonymouslyResponse>()
        println("authTAG signInAnonymously signInAnonymouslyResponse: $signInAnonymouslyResponse")
        emit(Result.Success("signInAnonymously"))
    }

    override suspend fun resetPassword(email: String) = flow {
        val httpResponse = authService.resetPassword(ResetPasswordBody(email, "PASSWORD_RESET"))
        val resetPasswordResponse = httpResponse.handleResponse<ResetPasswordResponse>()
        println("authTAG resetPassword resetPasswordResponse: $resetPasswordResponse")
        emit(Result.Success(Unit))
    }

    override fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ) = flow {
        val httpResponse = authService.createUserWithEmailAndPassword(AuthBody(email, password))
        val signUpEmailResponse = httpResponse.handleResponse<SignUpEmailResponse>()
        println("authTAG createUserWithEmailAndPassword signUpEmailResponse: $signUpEmailResponse")
        emit(Result.Success("signInEmailResponse"))
    }

    override fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Flow<Unit> = flow {
        // TODO implement id token
        val httpResponse = authService.changePassword(ChangePasswordBody(currentPassword, newPassword))
        val changePasswordResponse = httpResponse.handleResponse<ChangePasswordResponse>()
        println("authTAG changePassword changePasswordResponse: $changePasswordResponse")
        emit(Unit)
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