package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    suspend fun signInWithEmailAndPassword(email: String, password: String): Flow<Result<String>>

    suspend fun signInAnonymously(): Flow<Result<String>>

    suspend fun resetPassword(email: String): Flow<Result<Unit>>

    fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Result<String>>




    fun addAuthStateListener()

    fun removeAuthStateListener()

    fun fetchSignInMethodsForEmail(email: String): Flow<List<String>>

    fun signInWithGoogle(idToken: String): Flow<String>



    fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Flow<Unit>

    fun reAuthenticate(): Flow<Unit>

    fun deleteUser(): Flow<Unit>

    fun signOut(): Flow<Unit>

    fun isGoogleAuthUser(): Boolean

    fun googleSignOut()

    fun googleSignIn()
}