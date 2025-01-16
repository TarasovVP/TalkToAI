package com.vnteam.talktoai.domain.usecase

interface DataUseCase<in SetParams, out GetResult> {
    fun get(): GetResult
    suspend fun set(params: SetParams)
}