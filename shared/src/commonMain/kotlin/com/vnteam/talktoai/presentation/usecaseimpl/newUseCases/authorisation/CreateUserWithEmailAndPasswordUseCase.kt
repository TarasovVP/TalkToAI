package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.getDataOrNull
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState

class CreateUserWithEmailAndPasswordUseCase(
    private val repository: AuthRepository,
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository
) : UseCase<Pair<String, String>, Result<String>> {

    override suspend fun execute(params: Pair<String, String>): Result<String> {
        if (networkState.isNetworkAvailable().not()) {
            return Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
        }
        val authBody = AuthBody(params.first, params.second)
        val result = repository.createUserWithEmailAndPassword(authBody).getDataOrNull()
        result?.apply {
            preferencesRepository.setIdToken(idToken.orEmpty())
            preferencesRepository.setUserEmail(email.orEmpty())
        }
        return Result.Success(result?.email.orEmpty())
    }
}