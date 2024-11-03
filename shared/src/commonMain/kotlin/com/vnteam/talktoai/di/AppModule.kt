package com.vnteam.talktoai.di

import com.vnteam.talktoai.data.baseUrl
import com.vnteam.talktoai.data.database.dao.ChatDao
import com.vnteam.talktoai.data.database.dao.ChatDaoImpl
import com.vnteam.talktoai.data.database.SharedDatabase
import com.vnteam.talktoai.data.database.dao.MessageDao
import com.vnteam.talktoai.data.database.dao.MessageDaoImpl
import com.vnteam.talktoai.data.mapperimpls.ChatDBMapperImpl
import com.vnteam.talktoai.data.mapperimpls.MessageDBMapperImpl
import com.vnteam.talktoai.data.network.ApiService
import com.vnteam.talktoai.data.repositoryimpl.ChatRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.MessageRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.PreferencesRepositoryImpl
import com.vnteam.talktoai.domain.mappers.ChatDBMapper
import com.vnteam.talktoai.domain.mappers.ChatUIMapper
import com.vnteam.talktoai.domain.mappers.MessageDBMapper
import com.vnteam.talktoai.domain.mappers.MessageUIMapper
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.AppUseCase
import com.vnteam.talktoai.domain.usecase.ChatUseCase
import com.vnteam.talktoai.domain.usecase.MainUseCase
import com.vnteam.talktoai.presentation.mapperimpls.ChatUIMapperImpl
import com.vnteam.talktoai.presentation.mapperimpls.MessageUIMapperImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import com.vnteam.talktoai.presentation.usecaseimpl.AppUseCaseImpl
import com.vnteam.talktoai.presentation.usecaseimpl.ChatUseCaseImpl
import com.vnteam.talktoai.presentation.usecaseimpl.MainUseCaseImpl
import com.vnteam.talktoai.presentation.viewmodels.AppViewModel
import com.vnteam.talktoai.presentation.viewmodels.ChatViewModel
import com.vnteam.talktoai.presentation.viewmodels.MainViewModel
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import org.koin.core.module.dsl.viewModel

val appModule = module {

    single { baseUrl() }
    single { ApiService(get<String>(), get()) }
    single { Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    } }
    single {
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }
            install(ContentNegotiation) {
                json(get())
            }
        }
    }

    single {
        SharedDatabase(get())
    }

    single<ChatDao> {
        ChatDaoImpl(get())
    }

    single<MessageDao> {
        MessageDaoImpl(get())
    }

    single<ChatDBMapper> { ChatDBMapperImpl() }

    single<MessageDBMapper> { MessageDBMapperImpl() }

    single<ChatRepository> { ChatRepositoryImpl(get(), get()) }

    single<MessageRepository> { MessageRepositoryImpl(get(), get(), get()) }

    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

    single<ChatUIMapper> { ChatUIMapperImpl() }

    single<MessageUIMapper> { MessageUIMapperImpl() }

    single<AppUseCase> { AppUseCaseImpl(get()) }

    single<ChatUseCase> { ChatUseCaseImpl(get(), get(), get(), get(), get(), get()) }

    single<MainUseCase> { MainUseCaseImpl(get(), get(), get(), get(), get()) }

    viewModel {
        AppViewModel(get(), get())
    }
    viewModel {
        MainViewModel(get())
    }
    viewModel {
        ChatViewModel(get(), get(), get())
    }
}