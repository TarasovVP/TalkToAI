package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class CreateUserWithGoogleUseCase(private val repository: AuthRepository) :
    UseCase<String, Result<Unit>> {

    override suspend fun execute(params: String): Result<Unit> {
        return repository.googleSignIn(params)
    }
}