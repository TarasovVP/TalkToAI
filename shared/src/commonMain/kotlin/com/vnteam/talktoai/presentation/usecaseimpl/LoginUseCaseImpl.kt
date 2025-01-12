package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.Flow


class LoginUseCaseImpl(private val authRepository: AuthRepository) :
    LoginUseCase {

    override fun sendPasswordResetEmail(email: String): Flow<Result<Unit>> =
        authRepository.sendPasswordResetEmail(email)

    override fun fetchSignInMethodsForEmail(email: String): Flow<Result<List<String>>> =
        authRepository.fetchSignInMethodsForEmail(email)

    override fun signInWithEmailAndPassword(email: String, password: String): Flow<Result<Unit>> =
        authRepository.signInWithEmailAndPassword(email, password)

    override fun signInAuthWithGoogle(idToken: String): Flow<Result<Unit>> =
        authRepository.signInWithGoogle(idToken)

    override suspend fun signInAnonymously(): Flow<Result<Unit>> =
        authRepository.signInAnonymously()

    override fun googleSignOut() {
        authRepository.googleSignOut()
    }

    override fun googleSignIn() {
        authRepository.googleSignIn()
    }
}