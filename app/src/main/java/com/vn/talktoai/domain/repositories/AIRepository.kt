package com.vn.talktoai.domain.repositories

import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import kotlinx.coroutines.flow.Flow

interface AIRepository {

    fun getCompletions(apiRequest: ApiRequest): Flow<ApiResponse>
}