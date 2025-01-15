package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class CreateUserWithEmailAndPasswordUseCase(private val repository: AuthRepository) :
    UseCase<Pair<String, String>, Flow<Result<List<String>>>> {

    override suspend fun execute(params: Pair<String, String>): Flow<Result<List<String>>> {
        return repository.createUserWithEmailAndPassword(params.first, params.second).map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}