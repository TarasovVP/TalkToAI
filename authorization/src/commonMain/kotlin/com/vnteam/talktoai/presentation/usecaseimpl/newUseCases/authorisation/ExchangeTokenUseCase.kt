package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.response.ExchangeTokenResponse
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class ExchangeTokenUseCase(
    private val repository: AuthRepository,
) : UseCase<String, Result<ExchangeTokenResponse>> {

    override suspend fun execute(params: String): Result<ExchangeTokenResponse> {
        return repository.exchangeRefreshToken(params)
    }
}
