package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.Flow


class LoginUseCaseImpl(private val authRepository: AuthRepository) :
    LoginUseCase {

    override fun sendPasswordResetEmail(email: String): Flow<NetworkResult<Unit>> =
        authRepository.sendPasswordResetEmail(email)

    override fun fetchSignInMethodsForEmail(email: String): Flow<NetworkResult<List<String>>> =
        authRepository.fetchSignInMethodsForEmail(email)

    override fun signInWithEmailAndPassword(email: String, password: String): Flow<NetworkResult<Unit>> =
        authRepository.signInWithEmailAndPassword(email, password)

    override fun signInAuthWithGoogle(idToken: String): Flow<NetworkResult<Unit>> =
        authRepository.signInWithGoogle(idToken)

    override suspend fun signInAnonymously(): Flow<NetworkResult<Unit>> =
        authRepository.signInAnonymously()

    override fun googleSignOut() {
        authRepository.googleSignOut()
    }

    override fun googleSignIn() {
        authRepository.googleSignIn()
    }
}