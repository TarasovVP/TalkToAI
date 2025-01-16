package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class InsertRemoteUserUseCase(private val realDataBaseRepository: RealDataBaseRepository) :
    UseCase<RemoteUser, Flow<Result<Unit>>> {

    override suspend fun execute(params: RemoteUser): Flow<Result<Unit>> {
        return realDataBaseRepository.insertRemoteUser(params).map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}