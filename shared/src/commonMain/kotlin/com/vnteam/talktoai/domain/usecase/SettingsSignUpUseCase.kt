package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import kotlinx.coroutines.flow.Flow

interface SettingsSignUpUseCase {

    fun fetchSignInMethodsForEmail(email: String, result: (NetworkResult<List<String>>) -> Unit)

    fun createUserWithGoogle(idToken: String, result: (NetworkResult<Unit>) -> Unit)

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (NetworkResult<String>) -> Unit,
    )

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        result: (NetworkResult<Unit>) -> Unit
    )

    suspend fun getChats(): Flow<List<Chat>>

    suspend fun getMessages(): Flow<List<Message>>

    fun insertRemoteCurrentUser(remoteUser: RemoteUser, result: (NetworkResult<Unit>) -> Unit)

    fun updateRemoteCurrentUser(remoteUser: RemoteUser, result: (NetworkResult<Unit>) -> Unit)
}