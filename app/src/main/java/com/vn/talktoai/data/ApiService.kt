package com.vn.talktoai.data

import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("chat/completions")
    fun getCompletions(@Body apiRequest: ApiRequest): Flow<ApiResponse>
}