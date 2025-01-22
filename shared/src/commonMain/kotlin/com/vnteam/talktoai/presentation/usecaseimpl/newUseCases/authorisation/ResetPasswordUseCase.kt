package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.ResetPasswordBody
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf

class ResetPasswordUseCase(
    private val repository: AuthRepository,
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository
) : UseCase<String, Flow<Result<Unit>>> {

    override suspend fun execute(params: String): Flow<Result<Unit>> {
        return when {
            networkState.isNetworkAvailable() -> {
                val userLogin = preferencesRepository.getUserEmail().firstOrNull()
                val resetPasswordBody = ResetPasswordBody(userLogin, params)
                repository.resetPassword(resetPasswordBody)
            }
            else -> flowOf(Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT))
        }
    }
}