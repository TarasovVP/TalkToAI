package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.auth.response.SignUpEmailResponse
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class CreateUserWithEmailAndPasswordUseCase(
    private val repository: AuthRepository,
) : UseCase<Pair<String, String>, Result<SignUpEmailResponse>> {

    override suspend fun execute(params: Pair<String, String>): Result<SignUpEmailResponse> {
        val authBody = AuthBody(params.first, params.second)
        return repository.createUserWithEmailAndPassword(authBody)
    }
}