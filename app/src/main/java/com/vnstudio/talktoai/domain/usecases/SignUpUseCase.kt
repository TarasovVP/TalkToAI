package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.data.network.Result
import com.vnstudio.talktoai.domain.models.CurrentUser

interface SignUpUseCase {

    fun fetchSignInMethodsForEmail(email: String, result: (Result<List<String>>) -> Unit)

    fun createUserWithGoogle(idToken: String, result: (Result<Unit>) -> Unit)

    fun createUserWithEmailAndPassword(email: String, password: String, result: (Result<String>) -> Unit)

    fun createCurrentUser(currentUser: CurrentUser, result: (Result<Unit>) -> Unit)
}