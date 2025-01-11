package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.RemoteUser
import kotlinx.coroutines.flow.Flow

interface SignUpUseCase {

    fun fetchSignInMethodsForEmail(email: String): Flow<NetworkResult<List<String>>>

    fun createUserWithGoogle(idToken: String): Flow<NetworkResult<Unit>>

    fun createUserWithEmailAndPassword(
        email: String,
        password: String): Flow<NetworkResult<List<String>>>

    fun insertRemoteUser(remoteUser: RemoteUser): Flow<NetworkResult<Unit>>

    fun googleSignOut()

    fun googleSignIn()
}