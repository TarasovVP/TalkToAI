package com.vnteam.talktoai.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T? = null) : Result<T>()
    data class Failure(val errorMessage: String? = null) : Result<Nothing>()
}

fun <T> Flow<Result<T>>.onSuccess(action: (T?) -> Unit): Flow<Result<T>> = map { result ->
    if (result is Result.Success) {
        action(result.data)
    }
    result
}

fun <T> Flow<Result<T>>.onError(action: (String?) -> Unit): Flow<Result<T>> = map { result ->
    if (result is Result.Failure) {
        action(result.errorMessage)
    }
    result
}

suspend fun <T> Flow<Result<T>>.getDataOrNull(): T? {
    return when (val result = this.firstOrNull()) {
        is Result.Success -> result.data
        else -> null
    }
}

fun <T> Result<T>.getDataOrNull(): T? {
    return when(this) {
        is Result.Success -> this.data
        else -> null
    }
}
