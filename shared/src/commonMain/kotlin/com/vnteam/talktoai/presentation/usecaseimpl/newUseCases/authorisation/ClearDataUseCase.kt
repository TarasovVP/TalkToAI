package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ClearDataUseCase : UseCase<Nothing?, Flow<Result<Unit>>> {

    override suspend fun execute(params: Nothing?): Flow<Result<Unit>> {
        // TODO: Implement clear preferences and database
        return flowOf(Result.Success(Unit))
    }
}