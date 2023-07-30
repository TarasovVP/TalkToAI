package com.vn.talktoai

import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("chat/completions")
    fun getCompletions(@Body apiRequest: ApiRequest): Call<ApiResponse>
}