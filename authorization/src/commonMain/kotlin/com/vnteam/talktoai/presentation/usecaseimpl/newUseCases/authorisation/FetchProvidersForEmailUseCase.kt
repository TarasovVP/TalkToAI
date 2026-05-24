package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.ProvidersForEmailBody
import com.vnteam.talktoai.data.network.getDataOrNull
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class FetchProvidersForEmailUseCase(
    private val repository: AuthRepository,
) : UseCase<String?, Result<List<String>>> {

    override suspend fun execute(params: String?): Result<List<String>> {
        return when (val result = repository.fetchProvidersForEmail(
            ProvidersForEmailBody(identifier = params, continueUri = "https://localhost")
        )) {
            is Result.Success -> Result.Success(result.data?.allProviders.orEmpty())
            is Result.Failure -> result
            is Result.Loading -> result
        }
    }
}