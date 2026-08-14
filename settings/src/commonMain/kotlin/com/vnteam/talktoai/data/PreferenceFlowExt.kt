package com.vnteam.talktoai.data

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.asPreferenceResult(): Flow<Result<T>> =
    map { Result.Success(it) }
        .catch { emit(Result.Failure(it.message)) }
