package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class SignOutUseCase(private val repository: AuthRepository) :
    UseCase<Nothing?, Result<Unit>> {

    override suspend fun execute(params: Nothing?): Result<Unit> {
        return repository.googleSignOut()
    }
}