package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SignInAnonymouslyUseCase(
    private val repository: AuthRepository,
    private val networkState: NetworkState
) : UseCase<Nothing?, Flow<Result<String>>> {

    override suspend fun execute(params: Nothing?): Flow<Result<String>> {
        return when {
            networkState.isNetworkAvailable() -> repository.signInAnonymously()
            else -> flowOf(Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT))
        }
    }
}