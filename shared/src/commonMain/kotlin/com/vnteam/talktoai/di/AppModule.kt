package com.vnteam.talktoai.di

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.vnteam.talktoai.data.baseUrl
import com.vnteam.talktoai.data.database.dao.ChatDao
import com.vnteam.talktoai.data.database.dao.ChatDaoImpl
import com.vnteam.talktoai.data.database.SharedDatabase
import com.vnteam.talktoai.data.mapperimpls.ChatDBMapperImpl
import com.vnteam.talktoai.data.mapperimpls.MessageDBMapperImpl
import com.vnteam.talktoai.data.network.ApiService
import com.vnteam.talktoai.data.repositoryimpl.ChatDBRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.PreferencesRepositoryImpl
import com.vnteam.talktoai.domain.mappers.ChatDBMapper
import com.vnteam.talktoai.domain.mappers.MessageDBMapper
import com.vnteam.talktoai.domain.repositories.ChatDBRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.AppUseCase
import com.vnteam.talktoai.domain.usecase.CreateUseCase
import com.vnteam.talktoai.domain.usecase.DetailsUseCase
import com.vnteam.talktoai.domain.usecase.ListUseCase
import com.vnteam.talktoai.presentation.viewmodels.DetailsViewModel
import com.vnteam.talktoai.presentation.viewmodels.ListViewModel
import com.vnteam.talktoai.presentation.mapperimpls.DemoObjectUIMapperImpl
import com.vnteam.talktoai.presentation.mapperimpls.OwnerUIMapperImpl
import com.vnteam.talktoai.presentation.mappers.DemoObjectUIMapper
import com.vnteam.talktoai.presentation.usecaseimpl.ListUseCaseImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import com.vnteam.talktoai.presentation.mappers.OwnerUIMapper
import com.vnteam.talktoai.presentation.states.screen.ScreenState
import com.vnteam.talktoai.presentation.usecaseimpl.AppUseCaseImpl
import com.vnteam.talktoai.presentation.usecaseimpl.CreateUseCaseImpl
import com.vnteam.talktoai.presentation.usecaseimpl.DetailsUseCaseImpl
import com.vnteam.talktoai.presentation.viewmodels.AppViewModel
import com.vnteam.talktoai.presentation.viewmodels.CreateViewModel
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

    single<ChatDBMapper> { ChatDBMapperImpl() }

    single<MessageDBMapper> { MessageDBMapperImpl() }

    single<ChatDBRepository> { ChatDBRepositoryImpl(get(), get()) }

    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

    single<OwnerUIMapper> { OwnerUIMapperImpl() }

    single<DemoObjectUIMapper> { DemoObjectUIMapperImpl(get()) }

    single<AppUseCase> { AppUseCaseImpl(get()) }

    single<ListUseCase> { ListUseCaseImpl(get(), get()) }

    single<DetailsUseCase> { DetailsUseCaseImpl(get(), get()) }

    single<CreateUseCase> { CreateUseCaseImpl(get(), get()) }

    single<MutableState<ScreenState>> { mutableStateOf( ScreenState() ) }

    viewModel {
        AppViewModel(get(), get())
    }
    viewModel {
        ListViewModel(get(), get(), get())
    }
    viewModel {
        DetailsViewModel(get(), get(), get())
    }
    viewModel {
        CreateViewModel(get(), get(), get())
    }
}