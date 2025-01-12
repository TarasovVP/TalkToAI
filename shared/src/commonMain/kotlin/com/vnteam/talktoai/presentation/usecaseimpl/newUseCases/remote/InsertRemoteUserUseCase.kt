package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class InsertRemoteUserUseCase(private val realDataBaseRepository: RealDataBaseRepository) {

    fun execute(remoteUser: RemoteUser): Flow<Result<Unit>> {
        return realDataBaseRepository.insertRemoteUser(remoteUser).map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}