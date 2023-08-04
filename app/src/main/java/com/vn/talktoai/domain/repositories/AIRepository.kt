package com.vn.talktoai.domain.repositories

import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import kotlinx.coroutines.flow.Flow
import com.vn.talktoai.data.Result

interface AIRepository {

    suspend fun getCompletions(apiRequest: ApiRequest): Flow<Result<ApiResponse>>
}