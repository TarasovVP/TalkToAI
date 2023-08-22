package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.data.network.Result

interface LoginUseCase {

    fun sendPasswordResetEmail(email: String, result: (Result<Unit>) -> Unit)

    fun fetchSignInMethodsForEmail(email: String, result: (Result<List<String>>) -> Unit)

    fun signInWithEmailAndPassword(email: String, password: String, result: (Result<Unit>) -> Unit)

    fun signInAuthWithGoogle(idToken: String, result: (Result<Unit>) -> Unit)

    fun signInAnonymously(result: (Result<Unit>) -> Unit)
}