package com.vn.talktoai

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Client {

    private val SERVER_TIMEOUT_IN_SECONDS = 30L

    private var retrofit: Retrofit? = null

    private fun buildRetrofit(): Retrofit {
        return retrofit ?: Retrofit.Builder()
            .client(createHttpClient().build())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    fun createHttpClient(): OkHttpClient.Builder = OkHttpClient.Builder()
        .connectTimeout(SERVER_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .readTimeout(SERVER_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(SERVER_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })

    fun getRetrofit(): Retrofit {
        return buildRetrofit()
    }
}