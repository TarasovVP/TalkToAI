package com.vnteam.talktoai.di

import com.vnteam.talktoai.data.network.ai.AIHttpClient
import com.vnteam.talktoai.data.network.ai.AIService
import com.vnteam.talktoai.data.repositoryimpl.AIRepositoryImpl
import com.vnteam.talktoai.domain.repositories.AIRepository
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val aiModule = module {

    single(named("aiJson")) {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single { AIHttpClient(get(named("aiJson"))) }

    single { AIService(get()) }

    single<AIRepository> { AIRepositoryImpl(get()) }
}
