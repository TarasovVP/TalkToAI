package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import kotlinx.coroutines.flow.Flow

interface SettingsSignUpUseCase {

    fun fetchSignInMethodsForEmail(email: String): Flow<Result<List<String>>>

    fun createUserWithGoogle(idToken: String): Flow<Result<Unit>>

    fun createUserWithEmailAndPassword(email: String, password: String): Flow<Result<List<String>>>

    fun signInWithEmailAndPassword(email: String, password: String): Flow<Result<Unit>>

    suspend fun getChats(): Flow<Result<List<Chat>>>

    suspend fun getMessages(): Flow<Result<List<Message>>>

    fun insertRemoteCurrentUser(remoteUser: RemoteUser): Flow<Result<Unit>>

    fun updateRemoteCurrentUser(remoteUser: RemoteUser): Flow<Result<Unit>>

    fun googleSign()
}