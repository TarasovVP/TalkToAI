package com.vnteam.talktoai.di

import com.vnteam.talktoai.data.network.ai.AIHttpClient
import com.vnteam.talktoai.data.network.ai.AIService
import com.vnteam.talktoai.data.repositoryimpl.AIRepositoryImpl
import com.vnteam.talktoai.domain.repositories.AIRepository
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.ai.SendRequestUseCase
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val aiModule = module {

    single {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single { AIHttpClient(get()) }

    single { AIService(get()) }

    single<AIRepository> { AIRepositoryImpl(get()) }

    single { SendRequestUseCase(get()) }
}
