package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.SettingsSignUpUseCase

class SettingsSignUpUseCaseImpl(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
) : SettingsSignUpUseCase {

    override fun fetchSignInMethodsForEmail(
        email: String,
        result: (NetworkResult<List<String>>) -> Unit
    ) =
        authRepository.fetchSignInMethodsForEmail(email) { authResult ->
            result.invoke(authResult)
        }

    override fun createUserWithGoogle(idToken: String, result: (NetworkResult<Unit>) -> Unit) =
        authRepository.signInWithGoogle(idToken) { authResult ->
            result.invoke(authResult)
        }

    override fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (NetworkResult<String>) -> Unit,
    ) = authRepository.createUserWithEmailAndPassword(email, password) { authResult ->
        result.invoke(authResult)
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        result: (NetworkResult<Unit>) -> Unit,
    ) = authRepository.signInWithEmailAndPassword(email, password) { authResult ->
        result.invoke(authResult)
    }

    override suspend fun getChats() = chatRepository.getChats()

    override suspend fun getMessages() = messageRepository.getMessages()

    override fun insertRemoteCurrentUser(
        remoteUser: RemoteUser,
        result: (NetworkResult<Unit>) -> Unit
    ) =
        realDataBaseRepository.insertRemoteUser(remoteUser) { authResult ->
            result.invoke(authResult)
        }

    override fun updateRemoteCurrentUser(
        remoteUser: RemoteUser,
        result: (NetworkResult<Unit>) -> Unit
    ) =
        realDataBaseRepository.updateRemoteUser(remoteUser) { authResult ->
            result.invoke(authResult)
        }

    override fun googleSign() {
        authRepository.googleSignIn()
    }
}