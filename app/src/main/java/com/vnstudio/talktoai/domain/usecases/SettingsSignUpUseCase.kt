package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.sealed_classes.Result
import kotlinx.coroutines.flow.Flow

interface SettingsSignUpUseCase {

    fun fetchSignInMethodsForEmail(email: String, result: (Result<List<String>>) -> Unit)

    fun createUserWithGoogle(idToken: String, result: (Result<Unit>) -> Unit)

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (Result<String>) -> Unit,
    )

    fun signInWithEmailAndPassword(email: String, password: String, result: (Result<Unit>) -> Unit)

    suspend fun getChats(): Flow<List<Chat>>

    suspend fun getMessages(): Flow<List<Message>>

    fun insertRemoteCurrentUser(remoteUser: com.vnstudio.talktoai.domain.models.RemoteUser, result: (Result<Unit>) -> Unit)

    fun updateRemoteCurrentUser(remoteUser: com.vnstudio.talktoai.domain.models.RemoteUser, result: (Result<Unit>) -> Unit)
}