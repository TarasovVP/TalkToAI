package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.ProvidersForEmailBody
import com.vnteam.talktoai.data.network.getDataOrNull
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState

class FetchProvidersForEmailUseCase(
    private val repository: AuthRepository,
    private val networkState: NetworkState,
) : UseCase<String?, Result<List<String>>> {

    override suspend fun execute(params: String?): Result<List<String>> {
        if (networkState.isNetworkAvailable().not()) {
            return Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
        }
        val providersForEmailResponse =
            repository.fetchProvidersForEmail(ProvidersForEmailBody(params)).getDataOrNull()
        return Result.Success(providersForEmailResponse?.allProviders.orEmpty())
    }
}