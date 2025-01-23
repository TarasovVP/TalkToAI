package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.ResetPasswordBody
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState

class ResetPasswordUseCase(
    private val repository: AuthRepository,
    private val networkState: NetworkState
) : UseCase<String, Result<Unit>> {

    override suspend fun execute(params: String): Result<Unit> {
        if (networkState.isNetworkAvailable().not()) {
            return Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
        }
        val body = ResetPasswordBody(params, "PASSWORD_RESET")
        return when (val result = repository.resetPassword(body)) {
            is Result.Failure -> result
            is Result.Loading -> result
            is Result.Success -> Result.Success(Unit)
        }
    }
}