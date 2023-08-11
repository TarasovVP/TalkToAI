package com.vn.talktoai.data.network

import com.vn.talktoai.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.run {
            proceed(
                request()
                    .newBuilder().apply {
                        addHeader("Content-Type", "application/json")
                        addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
                        addHeader("OpenAI-Organization", BuildConfig.ORGANIZATION_ID)
                    }
                    .build()
            )
        }
    }
}