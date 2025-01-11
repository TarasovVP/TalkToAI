package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {

    fun sendPasswordResetEmail(email: String): Flow<NetworkResult<Unit>>

    fun fetchSignInMethodsForEmail(email: String): Flow<NetworkResult<List<String>>>

    fun signInWithEmailAndPassword(email: String, password: String): Flow<NetworkResult<Unit>>

    fun signInAuthWithGoogle(idToken: String): Flow<NetworkResult<Unit>>

    suspend fun signInAnonymously(): Flow<NetworkResult<Unit>>

    fun googleSignOut()

    fun googleSignIn()
}