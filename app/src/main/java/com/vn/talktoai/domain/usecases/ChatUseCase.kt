package com.vn.talktoai.domain.usecases

import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import kotlinx.coroutines.flow.Flow

interface ChatUseCase {

    fun getCompletions(apiRequest: ApiRequest): Flow<ApiResponse>
}