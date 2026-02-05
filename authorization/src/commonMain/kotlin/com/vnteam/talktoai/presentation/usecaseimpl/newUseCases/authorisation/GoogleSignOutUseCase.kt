package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class GoogleSignOutUseCase(
    private val repository: AuthRepository,
) : UseCase<Nothing?, Result<Unit>> {

    override suspend fun execute(params: Nothing?): Result<Unit> {
        return when (val result = repository.googleSignOut()) {
            is Result.Failure -> result
            is Result.Loading -> result
            is Result.Success -> Result.Success(Unit)
        }
    }
}