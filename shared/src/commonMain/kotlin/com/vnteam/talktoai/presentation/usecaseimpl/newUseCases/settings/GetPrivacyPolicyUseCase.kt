package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetPrivacyPolicyUseCase(private val remoteStoreRepository: RemoteStoreRepository) :
    UseCase<String, Flow<Result<String>>> {

    override suspend fun execute(params: String): Flow<Result<String>> {
        return remoteStoreRepository.getPrivacyPolicy(params).map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}