package com.vnteam.talktoai.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed class NetworkResult<out T> {
    data object Loading : NetworkResult<Nothing>()
    data class Success<out T>(val data: T? = null) : NetworkResult<T>()
    data class Failure(val errorMessage: String? = null) : NetworkResult<Nothing>()
}

fun <T> Flow<NetworkResult<T>>.onSuccess(action: (T?) -> Unit): Flow<NetworkResult<T>> = map { result ->
    if (result is NetworkResult.Success) {
        action(result.data)
    }
    result
}

fun <T> Flow<NetworkResult<T>>.onError(action: (String?) -> Unit): Flow<NetworkResult<T>> = map { result ->
    if (result is NetworkResult.Failure) {
        action(result.errorMessage)
    }
    result
}
