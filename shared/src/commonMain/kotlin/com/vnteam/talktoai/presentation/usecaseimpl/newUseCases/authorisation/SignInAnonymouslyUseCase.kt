package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SignInAnonymouslyUseCase(private val repository: AuthRepository) :
    UseCase<Nothing?, Flow<Result<String>>> {

    override suspend fun execute(params: Nothing?): Flow<Result<String>> {
        return repository.signInAnonymously().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}