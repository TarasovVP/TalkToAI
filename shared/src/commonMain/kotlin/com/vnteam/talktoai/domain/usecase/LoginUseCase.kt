package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {

    fun sendPasswordResetEmail(email: String, result: (NetworkResult<Unit>) -> Unit)

    fun fetchSignInMethodsForEmail(email: String, result: (NetworkResult<List<String>>) -> Unit)

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        result: (NetworkResult<Unit>) -> Unit
    )

    fun signInAuthWithGoogle(idToken: String): Flow<NetworkResult<Unit>>

    suspend fun signInAnonymously(): Flow<NetworkResult<Unit>>

    fun googleSignOut()

    fun googleSignIn()
}