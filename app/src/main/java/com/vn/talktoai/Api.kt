package com.vn.talktoai

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface Api {

    @POST("chat/completions")
    suspend fun getCompletions(@Body apiRequest: ApiRequest): Call<ApiResponse?>
}