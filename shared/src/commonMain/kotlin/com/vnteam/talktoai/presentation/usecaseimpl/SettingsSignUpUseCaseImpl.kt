package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.SettingsSignUpUseCase
import kotlinx.coroutines.flow.Flow

class SettingsSignUpUseCaseImpl(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
) : SettingsSignUpUseCase {

    override fun fetchSignInMethodsForEmail(email: String) =
        authRepository.fetchSignInMethodsForEmail(email)

    override fun createUserWithGoogle(idToken: String) =
        authRepository.signInWithGoogle(idToken)

    override fun createUserWithEmailAndPassword(email: String, password: String) =
        authRepository.createUserWithEmailAndPassword(email, password)

    override fun signInWithEmailAndPassword(email: String, password: String) =
        authRepository.signInWithEmailAndPassword(email, password)

    override suspend fun getChats() = chatRepository.getChats()

    override suspend fun getMessages() = messageRepository.getMessages()

    override fun insertRemoteCurrentUser(
        remoteUser: RemoteUser): Flow<NetworkResult<Unit>> =
        realDataBaseRepository.insertRemoteUser(remoteUser)

    override fun updateRemoteCurrentUser(
        remoteUser: RemoteUser): Flow<NetworkResult<Unit>> =
        realDataBaseRepository.updateRemoteUser(remoteUser)

    override fun googleSign() {
        authRepository.googleSignIn()
    }
}