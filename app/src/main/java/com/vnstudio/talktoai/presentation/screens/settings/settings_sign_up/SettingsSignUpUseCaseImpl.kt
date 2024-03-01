package com.vnstudio.talktoai.presentation.screens.settings.settings_sign_up

import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.repositories.AuthRepository
import com.vnstudio.talktoai.domain.repositories.ChatRepository
import com.vnstudio.talktoai.domain.repositories.MessageRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsSignUpUseCase


class SettingsSignUpUseCaseImpl(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
) : SettingsSignUpUseCase {

    override fun fetchSignInMethodsForEmail(email: String, result: (Result<List<String>>) -> Unit) =
        authRepository.fetchSignInMethodsForEmail(email) { authResult ->
            result.invoke(authResult)
        }

    override fun createUserWithGoogle(idToken: String, result: (Result<Unit>) -> Unit) =
        authRepository.signInWithGoogle(idToken) { authResult ->
            result.invoke(authResult)
        }

    override fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (Result<String>) -> Unit,
    ) = authRepository.createUserWithEmailAndPassword(email, password) { authResult ->
        result.invoke(authResult)
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        result: (Result<Unit>) -> Unit,
    ) = authRepository.signInWithEmailAndPassword(email, password) { authResult ->
        result.invoke(authResult)
    }

    override suspend fun getChats() = chatRepository.getChats()

    override suspend fun getMessages() = messageRepository.getMessages()

    override fun insertRemoteCurrentUser(remoteUser: RemoteUser, result: (Result<Unit>) -> Unit) =
        realDataBaseRepository.insertRemoteUser(remoteUser) { authResult ->
            result.invoke(authResult)
        }

    override fun updateRemoteCurrentUser(remoteUser: RemoteUser, result: (Result<Unit>) -> Unit) =
        realDataBaseRepository.updateRemoteUser(remoteUser) { authResult ->
            result.invoke(authResult)
        }
}