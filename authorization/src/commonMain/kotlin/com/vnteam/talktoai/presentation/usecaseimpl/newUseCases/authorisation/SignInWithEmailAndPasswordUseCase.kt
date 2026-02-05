package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.AuthBody
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class SignInWithEmailAndPasswordUseCase(
    private val repository: AuthRepository,
    private val preferencesRepository: PreferencesRepository,
) : UseCase<Pair<String, String>, Result<Unit>> {

    override suspend fun execute(params: Pair<String, String>): Result<Unit> {
        return when (val result =
            repository.signInWithEmailAndPassword(AuthBody(params.first, params.second))) {
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