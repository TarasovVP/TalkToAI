package com.vn.talktoai.domain.usecases

import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import kotlinx.coroutines.flow.Flow
import com.vn.talktoai.data.Result

interface ChatUseCase {

    suspend fun getCompletions(apiRequest: ApiRequest): Flow<Result<ApiResponse>>
}