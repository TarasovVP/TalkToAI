package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.ChangePasswordBody
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.firstOrNull

class ChangePasswordUseCase(
    private val repository: AuthRepository,
    private val preferencesRepository: PreferencesRepository,
) : UseCase<Pair<String, String>, Result<Unit>> {

    override suspend fun execute(params: Pair<String, String>): Result<Unit> {
        // TODO implement current password check
        val idToken = preferencesRepository.getIdToken().firstOrNull()
        val body = ChangePasswordBody(idToken, params.second)
        return when (val result = repository.changePassword(body)) {
            is Result.Failure -> result
            is Result.Loading -> result
            is Result.Success -> Result.Success(Unit)
        }
    }
}