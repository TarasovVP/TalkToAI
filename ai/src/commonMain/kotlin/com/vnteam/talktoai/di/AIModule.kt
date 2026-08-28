package com.vnteam.talktoai.di

import com.vnteam.talktoai.data.network.ai.anthropic.AnthropicHttpClient
import com.vnteam.talktoai.data.network.ai.anthropic.AnthropicProvider
import com.vnteam.talktoai.data.network.ai.anthropic.AnthropicService
import com.vnteam.talktoai.data.network.ai.openai.OpenAiHttpClient
import com.vnteam.talktoai.data.network.ai.openai.OpenAiProvider
import com.vnteam.talktoai.data.network.ai.openai.OpenAiService
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

    single { OpenAiHttpClient(get(named("aiJson"))) }
    single { OpenAiService(get()) }
    single { OpenAiProvider(get()) }

    single { AnthropicHttpClient(get(named("aiJson"))) }
    single { AnthropicService(get()) }
    single { AnthropicProvider(get()) }

    single<AIRepository> { AIRepositoryImpl(get(), get()) }
}
