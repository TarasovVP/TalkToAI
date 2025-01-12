package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    fun addAuthStateListener()

    fun removeAuthStateListener()

    fun isLoggedInUser(): Boolean

    fun isAuthorisedUser(): Boolean

    fun isGoogleAuthUser(): Boolean

    fun currentUserEmail(): String

    fun sendPasswordResetEmail(email: String): Flow<Result<Unit>>

    fun fetchSignInMethodsForEmail(email: String): Flow<Result<List<String>>>

    fun signInWithEmailAndPassword(email: String, password: String): Flow<Result<Unit>>

    fun signInWithGoogle(idToken: String): Flow<Result<Unit>>

    suspend fun signInAnonymously(): Flow<Result<Unit>>

    fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Result<List<String>>>

    fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Flow<Result<Unit>>

    fun reAuthenticate(): Flow<Result<Unit>>

    fun deleteUser(): Flow<Result<Unit>>

    fun signOut(): Flow<Result<Unit>>

    fun googleSignOut()

    fun googleSignIn()
}