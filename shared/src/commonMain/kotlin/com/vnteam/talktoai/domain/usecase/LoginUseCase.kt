package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult

interface LoginUseCase {

    fun sendPasswordResetEmail(email: String, result: (NetworkResult<Unit>) -> Unit)

    fun fetchSignInMethodsForEmail(email: String, result: (NetworkResult<List<String>>) -> Unit)

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        result: (NetworkResult<Unit>) -> Unit
    )

    fun signInAuthWithGoogle(idToken: String, result: (NetworkResult<Unit>) -> Unit)

    fun signInAnonymously(result: (NetworkResult<Unit>) -> Unit)

    fun googleSignOut()

    fun googleSignIn()
}