package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {

    fun sendPasswordResetEmail(email: String): Flow<Result<Unit>>

    fun fetchSignInMethodsForEmail(email: String): Flow<Result<List<String>>>

    fun signInWithEmailAndPassword(email: String, password: String): Flow<Result<Unit>>

    fun signInAuthWithGoogle(idToken: String): Flow<Result<Unit>>

    suspend fun signInAnonymously(): Flow<Result<Unit>>

    fun googleSignOut()

    fun googleSignIn()
}