package com.vn.talktoai.data.network

import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("chat/completions")
    suspend fun sendRequest(@Body apiRequest: ApiRequest): Response<ApiResponse>
}