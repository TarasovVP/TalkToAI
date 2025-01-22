package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.auth.request.ChangePasswordBody
import com.vnteam.talktoai.data.network.auth.request.ResetPasswordBody
import com.vnteam.talktoai.data.network.auth.response.ChangePasswordResponse
import com.vnteam.talktoai.data.network.auth.response.ResetPasswordResponse
import com.vnteam.talktoai.data.network.auth.response.SignInAnonymouslyResponse
import com.vnteam.talktoai.data.network.auth.response.SignInEmailResponse
import com.vnteam.talktoai.data.network.auth.response.SignUpEmailResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signInWithEmailAndPassword(authBody: AuthBody): Result<SignInEmailResponse>

    suspend fun signInAnonymously(authBody: AuthBody): Result<SignInAnonymouslyResponse>

    suspend fun resetPassword(resetPasswordBody: ResetPasswordBody): Result<ResetPasswordResponse>

    suspend fun createUserWithEmailAndPassword(authBody: AuthBody): Result<SignUpEmailResponse>

    suspend fun changePassword(changePasswordBody: ChangePasswordBody): Result<ChangePasswordResponse>




    fun addAuthStateListener()

    fun removeAuthStateListener()

    fun fetchSignInMethodsForEmail(email: String): Flow<List<String>>

    fun signInWithGoogle(idToken: String): Flow<String>





    fun reAuthenticate(): Flow<Unit>

    fun deleteUser(): Flow<Unit>

    fun signOut(): Flow<Unit>

    fun isGoogleAuthUser(): Boolean

    fun googleSignOut()

    fun googleSignIn()
}