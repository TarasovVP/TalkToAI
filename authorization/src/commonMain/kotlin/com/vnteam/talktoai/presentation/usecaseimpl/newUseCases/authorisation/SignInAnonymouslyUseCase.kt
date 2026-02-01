package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class SignInAnonymouslyUseCase(
    private val repository: AuthRepository,
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository,
) : UseCase<Nothing?, Result<Unit>> {

    override suspend fun execute(params: Nothing?): Result<Unit> {
        if (networkState.isNetworkAvailable().not()) {
            return Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
        }
        return when (val result = repository.signInAnonymously(AuthBody())) {
            is Result.Failure -> result
            is Result.Loading -> result
            is Result.Success -> {
                result.data?.apply {
                    preferencesRepository.setIdToken(idToken.orEmpty())
                    preferencesRepository.setUserEmail(email.orEmpty())
                }
                Result.Success(Unit)
            }
        }
    }
}