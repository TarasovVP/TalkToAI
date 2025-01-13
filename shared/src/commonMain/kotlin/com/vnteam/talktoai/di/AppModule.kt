package com.vnteam.talktoai.di

import com.vnteam.talktoai.data.database.SharedDatabase
import com.vnteam.talktoai.data.database.dao.ChatDao
import com.vnteam.talktoai.data.database.dao.ChatDaoImpl
import com.vnteam.talktoai.data.database.dao.MessageDao
import com.vnteam.talktoai.data.database.dao.MessageDaoImpl
import com.vnteam.talktoai.data.mapperimpls.ChatDBMapperImpl
import com.vnteam.talktoai.data.mapperimpls.MessageDBMapperImpl
import com.vnteam.talktoai.data.network.ApiService
import com.vnteam.talktoai.data.repositoryimpl.AuthRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.ChatRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.MessageRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.PreferencesRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.RealDataBaseRepositoryImpl
import com.vnteam.talktoai.domain.mappers.ChatDBMapper
import com.vnteam.talktoai.domain.mappers.ChatUIMapper
import com.vnteam.talktoai.domain.mappers.MessageDBMapper
import com.vnteam.talktoai.domain.mappers.MessageUIMapper
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.ChatUseCase
import com.vnteam.talktoai.presentation.mapperimpls.ChatUIMapperImpl
import com.vnteam.talktoai.presentation.mapperimpls.MessageUIMapperImpl
import com.vnteam.talktoai.presentation.usecaseimpl.ChatUseCaseImpl
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithGoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.FeedbackUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.FetchSignInMethodsForEmailUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.InsertRemoteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.LanguageUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ResetPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ReviewUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInAnonymouslyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithGoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ThemeUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserLoginUseCase
import com.vnteam.talktoai.presentation.viewmodels.AppViewModel
import com.vnteam.talktoai.presentation.viewmodels.ChatListViewModel
import com.vnteam.talktoai.presentation.viewmodels.ChatViewModel
import com.vnteam.talktoai.presentation.viewmodels.LoginViewModel
import com.vnteam.talktoai.presentation.viewmodels.OnBoardingViewModel
import com.vnteam.talktoai.presentation.viewmodels.SettingsAccountViewModel
import com.vnteam.talktoai.presentation.viewmodels.SettingsChatViewModel
import com.vnteam.talktoai.presentation.viewmodels.SettingsFeedbackViewModel
import com.vnteam.talktoai.presentation.viewmodels.SettingsLanguageViewModel
import com.vnteam.talktoai.presentation.viewmodels.SettingsPrivacyPolicyViewModel
import com.vnteam.talktoai.presentation.viewmodels.SettingsSignUpViewModel
import com.vnteam.talktoai.presentation.viewmodels.SettingsThemeViewModel
import com.vnteam.talktoai.presentation.viewmodels.SignUpViewModel
import com.vnteam.talktoai.secrets.Config.API_KEY
import com.vnteam.talktoai.secrets.Config.BASE_URL
import com.vnteam.talktoai.secrets.Config.ORGANIZATION_ID
import com.vnteam.talktoai.secrets.Config.PROJECT_ID
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { ApiService(BASE_URL, get()) }
    single {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            install(DefaultRequest) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $API_KEY")
                header("OpenAI-Organization", ORGANIZATION_ID)
                header("OpenAI-Project", PROJECT_ID)
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Logger Ktor => $message")
                    }
                }
                level = LogLevel.ALL
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

    single<AuthRepository> { AuthRepositoryImpl() }

    single<RealDataBaseRepository> { RealDataBaseRepositoryImpl() }

    single<ChatRepository> { ChatRepositoryImpl(get(), get()) }

    single<MessageRepository> { MessageRepositoryImpl(get(), get(), get()) }

    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

    single<ChatUIMapper> { ChatUIMapperImpl() }

    single<MessageUIMapper> { MessageUIMapperImpl() }

    // UseCases
    single<ThemeUseCase> { ThemeUseCase(get()) }

    single<LanguageUseCase> { LanguageUseCase(get()) }

    single<OnboardingUseCase> { OnboardingUseCase(get()) }

    single<UserLoginUseCase> { UserLoginUseCase(get()) }

    single<ReviewUseCase> { ReviewUseCase(get()) }

    single { FeedbackUseCase(get()) }

    single { ResetPasswordUseCase(get()) }

    single { SignInWithEmailAndPasswordUseCase(get()) }

    single { SignInWithGoogleUseCase(get()) }

    single { SignInAnonymouslyUseCase(get()) }

    single { FetchSignInMethodsForEmailUseCase(get()) }

    single { CreateUserWithEmailAndPasswordUseCase(get()) }

    single { CreateUserWithGoogleUseCase(get()) }

    single { GoogleUseCase(get()) }

    single { InsertRemoteUserUseCase(get()) }

    single<ChatUseCase> { ChatUseCaseImpl(get(), get(), get(), get(), get(), get()) }

    single<MainUseCase> { MainUseCaseImpl(get(), get(), get(), get()) }

    single<SettingsAccountUseCase> {
        SettingsAccountUseCaseImpl(get(), get(), get())
    }

    // ViewModels
    viewModel {
        AppViewModel(get(), get(), get(), get(), get())
    }
    viewModel {
        OnBoardingViewModel(get())
    }
    viewModel {
        LoginViewModel(get(), get(), get(), get(), get(), get(), get(), get())
    }
    viewModel {
        SignUpViewModel(get(), get(), get(), get(), get(), get())
    }
    viewModel {
        ChatListViewModel(get(), get())
    }
    viewModel {
        ChatViewModel(get(), get(), get(), get(), get(), get())
    }
    viewModel {
        SettingsAccountViewModel(get(), get(), get())
    }
    viewModel {
        SettingsChatViewModel(get(), get())
    }
    viewModel {
        SettingsFeedbackViewModel(get(), get(), get())
    }
    viewModel {
        SettingsLanguageViewModel(get())
    }
    viewModel {
        SettingsPrivacyPolicyViewModel(get(), get())
    }
    viewModel {
        SettingsSignUpViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
    viewModel {
        SettingsThemeViewModel(get())
    }
}