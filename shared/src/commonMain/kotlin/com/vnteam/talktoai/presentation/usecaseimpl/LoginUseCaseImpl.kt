package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.LoginUseCase


class LoginUseCaseImpl(private val authRepository: AuthRepository) :
    LoginUseCase {

    override fun sendPasswordResetEmail(email: String, result: (NetworkResult<Unit>) -> Unit) =
        authRepository.sendPasswordResetEmail(email) { authResult ->
            result.invoke(authResult)
        }

    override fun fetchSignInMethodsForEmail(
        email: String,
        result: (NetworkResult<List<String>>) -> Unit
    ) =
        authRepository.fetchSignInMethodsForEmail(email) { authResult ->
            result.invoke(authResult)
        }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        result: (NetworkResult<Unit>) -> Unit,
    ) = authRepository.signInWithEmailAndPassword(email, password) { authResult ->
        result.invoke(authResult)
    }

    override fun signInAuthWithGoogle(idToken: String, result: (NetworkResult<Unit>) -> Unit) =
        authRepository.signInWithGoogle(idToken) { authResult ->
            result.invoke(authResult)
        }

    override fun signInAnonymously(result: (NetworkResult<Unit>) -> Unit) =
        authRepository.signInAnonymously { authResult ->
            result.invoke(authResult)
        }
}