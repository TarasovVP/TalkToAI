package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FetchSignInMethodsForEmailUseCase(private val repository: AuthRepository) :
    UseCase<String, Flow<Result<List<String>>>> {

    override suspend fun execute(params: String): Flow<Result<List<String>>> {
        return repository.fetchSignInMethodsForEmail(params).map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}