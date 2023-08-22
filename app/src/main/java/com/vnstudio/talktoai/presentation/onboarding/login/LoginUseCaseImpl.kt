package com.vnstudio.talktoai.presentation.onboarding.login

import com.vnstudio.talktoai.data.network.Result
import com.vnstudio.talktoai.domain.repositories.AuthRepository
import com.vnstudio.talktoai.domain.usecases.LoginUseCase

import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(private val authRepository: AuthRepository):
    LoginUseCase {

    override fun sendPasswordResetEmail(email: String, result: (Result<Unit>) -> Unit) = authRepository.sendPasswordResetEmail(email) { authResult ->
        result.invoke(authResult)
    }

    override fun fetchSignInMethodsForEmail(email: String, result: (Result<List<String>>) -> Unit) = authRepository.fetchSignInMethodsForEmail(email) { authResult ->
        result.invoke(authResult)
    }

    override fun signInWithEmailAndPassword(email: String, password: String, result: (Result<Unit>) -> Unit) = authRepository.signInWithEmailAndPassword(email, password) { authResult ->
        result.invoke(authResult)
    }

    override fun signInAuthWithGoogle(idToken: String, result: (Result<Unit>) -> Unit) = authRepository.signInWithGoogle(idToken) { authResult ->
        result.invoke(authResult)
    }

    override fun signInAnonymously(result: (Result<Unit>) -> Unit) = authRepository.signInAnonymously { authResult ->
        result.invoke(authResult)
    }
}