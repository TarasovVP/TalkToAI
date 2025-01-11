package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.Flow

class SignUpUseCaseImpl(
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
) : SignUpUseCase {

    override fun fetchSignInMethodsForEmail(email: String): Flow<NetworkResult<List<String>>> =
        authRepository.fetchSignInMethodsForEmail(email)

    override fun createUserWithGoogle(idToken: String): Flow<NetworkResult<Unit>> =
        authRepository.signInWithGoogle(idToken)

    override fun createUserWithEmailAndPassword(
        email: String,
        password: String): Flow<NetworkResult<List<String>>> = authRepository.createUserWithEmailAndPassword(email, password)

    override fun insertRemoteUser(remoteUser: RemoteUser): Flow<NetworkResult<Unit>> =
        realDataBaseRepository.insertRemoteUser(remoteUser)

    override fun googleSignOut() {
        authRepository.googleSignOut()
    }

    override fun googleSignIn() {
        authRepository.googleSignIn()
    }
}