package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class CreateUserWithEmailAndPasswordUseCase(private val repository: AuthRepository) {

    fun execute(email: String, password: String): Flow<Result<List<String>>> {
        return repository.createUserWithEmailAndPassword(email, password).map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}