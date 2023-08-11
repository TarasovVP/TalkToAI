package com.vn.talktoai.data

import com.vn.talktoai.CommonExtensions.apiCall
import com.vn.talktoai.domain.repositories.AIRepository
import com.vn.talktoai.domain.ApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(private val apiService: ApiService) : AIRepository {

    override suspend fun getCompletions(apiRequest: ApiRequest) = flow {
        emit(apiService.getCompletions(apiRequest).apiCall())
    }.flowOn(Dispatchers.IO)
}