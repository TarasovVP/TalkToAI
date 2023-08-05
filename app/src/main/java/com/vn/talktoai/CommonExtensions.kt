package com.vn.talktoai

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private fun <T> LiveData<T>.safeObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
        this.observe(owner) {
            it?.let(observer)
        }
    }

    fun <T> MutableLiveData<T>.safeSingleObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
        safeObserve(owner, observer)
        value = null
    }
}