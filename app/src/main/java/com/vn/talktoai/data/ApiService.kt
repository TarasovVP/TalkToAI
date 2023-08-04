package com.vn.talktoai.data

import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("chat/completions")
    suspend fun getCompletions(@Body apiRequest: ApiRequest): Response<ApiResponse>
}