package com.vnteam.talktoai.domain.usecase

interface UseCase<in Params, out Result> {
    suspend fun execute(params: Params): Result
}