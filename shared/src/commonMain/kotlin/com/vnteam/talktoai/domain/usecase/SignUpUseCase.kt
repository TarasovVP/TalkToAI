package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.RemoteUser

interface SignUpUseCase {

    fun fetchSignInMethodsForEmail(email: String, result: (NetworkResult<List<String>>) -> Unit)

    fun createUserWithGoogle(idToken: String, result: (NetworkResult<Unit>) -> Unit)

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (NetworkResult<String>) -> Unit,
    )

    fun insertRemoteUser(remoteUser: RemoteUser, result: (NetworkResult<Unit>) -> Unit)

    fun googleSignOut()

    fun googleSignIn()
}