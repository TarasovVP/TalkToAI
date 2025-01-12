package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SignOutUseCase(private val repository: AuthRepository) {

    fun execute(): Flow<Result<Unit>> {
        return repository.signOut().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}