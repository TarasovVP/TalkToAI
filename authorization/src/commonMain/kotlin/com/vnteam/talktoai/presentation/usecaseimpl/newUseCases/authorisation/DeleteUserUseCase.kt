package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.DeleteAccountBody
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.firstOrNull

class DeleteUserUseCase(
    private val repository: AuthRepository,
    private val preferencesRepository: PreferencesRepository,
) : UseCase<Nothing?, Result<Unit>> {

    override suspend fun execute(params: Nothing?): Result<Unit> {
        val idToken = preferencesRepository.getIdToken().firstOrNull()
        return repository.deleteUser(DeleteAccountBody(idToken))
    }
}