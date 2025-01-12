package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.RemoteUser
import kotlinx.coroutines.flow.Flow

interface SignUpUseCase {

    fun fetchSignInMethodsForEmail(email: String): Flow<Result<List<String>>>

    fun createUserWithGoogle(idToken: String): Flow<Result<Unit>>

    fun createUserWithEmailAndPassword(
        email: String,
        password: String): Flow<Result<List<String>>>

    fun insertRemoteUser(remoteUser: RemoteUser): Flow<Result<Unit>>

    fun googleSignOut()

    fun googleSignIn()
}