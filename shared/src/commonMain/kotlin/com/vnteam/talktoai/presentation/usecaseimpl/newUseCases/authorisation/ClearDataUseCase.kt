package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ClearDataUseCase() {

    fun execute(): Flow<Result<Unit>> {
        // TODO: Implement clear preferences and database
        return flowOf( Result.Success(Unit) )
    }
}