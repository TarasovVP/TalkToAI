package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class ReAuthenticateUseCase(private val repository: AuthRepository) :
    UseCase<Pair<String, String>, Result<String?>> {

    override suspend fun execute(params: Pair<String, String>): Result<String?> {
        val (email, password) = params
        return when (val result = repository.reAuthenticate(AuthBody(email, password, returnSecureToken = true))) {
            is Result.Success -> Result.Success(result.data?.idToken)
            is Result.Failure -> Result.Failure(result.errorMessage)
            is Result.Loading -> Result.Loading
        }
    }
}
