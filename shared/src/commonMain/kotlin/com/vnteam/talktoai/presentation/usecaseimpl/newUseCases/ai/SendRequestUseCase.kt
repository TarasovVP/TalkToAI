package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.ai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.request.ApiRequest
import com.vnteam.talktoai.data.network.responses.ApiResponse
import com.vnteam.talktoai.domain.repositories.AIRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class SendRequestUseCase(private val aiRepository: AIRepository) :
    UseCase<ApiRequest, Flow<Result<ApiResponse>>> {

    override suspend fun execute(params: ApiRequest): Flow<Result<ApiResponse>> {
        return aiRepository.sendRequest(params)
    }
}