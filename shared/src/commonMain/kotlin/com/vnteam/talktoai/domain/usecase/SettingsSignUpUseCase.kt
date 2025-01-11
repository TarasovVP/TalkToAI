package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import kotlinx.coroutines.flow.Flow

interface SettingsSignUpUseCase {

    fun fetchSignInMethodsForEmail(email: String): Flow<NetworkResult<List<String>>>

    fun createUserWithGoogle(idToken: String): Flow<NetworkResult<Unit>>

    fun createUserWithEmailAndPassword(email: String, password: String): Flow<NetworkResult<List<String>>>

    fun signInWithEmailAndPassword(email: String, password: String): Flow<NetworkResult<Unit>>

    suspend fun getChats(): Flow<NetworkResult<List<Chat>>>

    suspend fun getMessages(): Flow<NetworkResult<List<Message>>>

    fun insertRemoteCurrentUser(remoteUser: RemoteUser): Flow<NetworkResult<Unit>>

    fun updateRemoteCurrentUser(remoteUser: RemoteUser): Flow<NetworkResult<Unit>>

    fun googleSign()
}