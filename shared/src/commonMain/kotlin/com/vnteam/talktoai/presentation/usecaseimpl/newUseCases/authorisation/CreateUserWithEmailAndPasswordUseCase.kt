package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.data.network.getDataOrNull
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class CreateUserWithEmailAndPasswordUseCase(
    private val repository: AuthRepository,
    private val preferencesRepository: PreferencesRepository
) : UseCase<Pair<String, String>, Result<String>> {

    override suspend fun execute(params: Pair<String, String>): Result<String> {
        val authBody = AuthBody(params.first, params.second)
        val result = repository.createUserWithEmailAndPassword(authBody).getDataOrNull()
        result?.apply {
            preferencesRepository.setIdToken(idToken.orEmpty())
            preferencesRepository.setUserEmail(email.orEmpty())
        }
        return result
    }
}