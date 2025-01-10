package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    fun addAuthStateListener()

    fun removeAuthStateListener()

    fun isLoggedInUser(): Boolean

    fun isAuthorisedUser(): Boolean

    fun isGoogleAuthUser(): Boolean

    fun currentUserEmail(): String

    fun sendPasswordResetEmail(email: String, result: (NetworkResult<Unit>) -> Unit)

    fun fetchSignInMethodsForEmail(email: String, result: (NetworkResult<List<String>>) -> Unit)

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        result: (NetworkResult<Unit>) -> Unit
    )

    fun signInWithGoogle(idToken: String): Flow<NetworkResult<Unit>>

    suspend fun signInAnonymously(): Flow<NetworkResult<Unit>>

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (NetworkResult<String>) -> Unit,
    )

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        result: (NetworkResult<Unit>) -> Unit
    )

    fun reAuthenticate()

    fun deleteUser(result: (NetworkResult<Unit>) -> Unit)

    fun signOut(result: (NetworkResult<Unit>) -> Unit)

    fun googleSignOut()

    fun googleSignIn()
}