package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class CreateUserWithEmailAndPasswordUseCase(private val repository: AuthRepository) :
    UseCase<Pair<String, String>, Flow<Result<String>>> {

    override suspend fun execute(params: Pair<String, String>): Flow<Result<String>> {
        return repository.createUserWithEmailAndPassword(params.first, params.second)
    }
}