package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.ChangePasswordBody
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class ChangePasswordUseCase(
    private val repository: AuthRepository,
    private val preferencesRepository: PreferencesRepository
) : UseCase<Pair<String, String>, Flow<Result<Unit>>> {

    override suspend fun execute(params: Pair<String, String>): Flow<Result<Unit>> {
        // TODO implement current password check
        val idToken = preferencesRepository.getIdToken().firstOrNull()
        return repository.changePassword(ChangePasswordBody(idToken, params.second))
    }
}