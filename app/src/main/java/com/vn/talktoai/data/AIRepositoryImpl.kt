package com.vn.talktoai.data

import com.vn.talktoai.domain.repositories.AIRepository
import com.vn.talktoai.domain.ApiRequest
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(private val apiService: ApiService) : AIRepository {

    override fun getCompletions(apiRequest: ApiRequest) = apiService.getCompletions(apiRequest)
}