package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.auth.response.SignInAnonymouslyResponse
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class SignInAnonymouslyUseCase(
    private val repository: AuthRepository,
) : UseCase<Nothing?, Result<SignInAnonymouslyResponse>> {

    override suspend fun execute(params: Nothing?): Result<SignInAnonymouslyResponse> {
        return repository.signInAnonymously(AuthBody())
    }
}