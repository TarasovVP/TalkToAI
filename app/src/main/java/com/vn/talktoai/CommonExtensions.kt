package com.vn.talktoai

import retrofit2.Response
import com.vn.talktoai.data.Result

object CommonExtensions {

    fun <T>  Response<T>.apiCall(): Result<T> {
        try {
            if (isSuccessful) {
                body()?.let { body ->
                    return Result.Success(body)
                }
            }
            return Result.Failure(message())
        } catch (e: Exception) {
            return Result.Failure(e.localizedMessage)
        }
    }
}