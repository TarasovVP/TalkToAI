package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.auth.response.SignInEmailResponse
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class SignInWithEmailAndPasswordUseCase(
    private val repository: AuthRepository,
) : UseCase<Pair<String, String>, Result<SignInEmailResponse>> {

    override suspend fun execute(params: Pair<String, String>): Result<SignInEmailResponse> {
        return repository.signInWithEmailAndPassword(AuthBody(params.first, params.second))
    }
}