package com.vnteam.talktoai.di

import com.vnteam.talktoai.data.database.SharedDatabase
import com.vnteam.talktoai.data.database.dao.ChatDao
import com.vnteam.talktoai.data.database.dao.ChatDaoImpl
import com.vnteam.talktoai.data.database.dao.MessageDao
import com.vnteam.talktoai.data.database.dao.MessageDaoImpl
import com.vnteam.talktoai.data.mapperimpls.ChatDBMapperImpl
import com.vnteam.talktoai.data.mapperimpls.MessageDBMapperImpl
import com.vnteam.talktoai.data.network.ai.AIService
import com.vnteam.talktoai.data.network.auth.AuthService
import com.vnteam.talktoai.data.repositoryimpl.AIRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.AuthRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.ChatRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.MessageRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.PreferencesRepositoryImpl
import com.vnteam.talktoai.data.repositoryimpl.RealDataBaseRepositoryImpl
import com.vnteam.talktoai.domain.mappers.ChatDBMapper
import com.vnteam.talktoai.domain.mappers.ChatUIMapper
import com.vnteam.talktoai.domain.mappers.MessageDBMapper
import com.vnteam.talktoai.domain.mappers.MessageUIMapper
import com.vnteam.talktoai.domain.repositories.AIRepository
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.presentation.mapperimpls.ChatUIMapperImpl
import com.vnteam.talktoai.presentation.mapperimpls.MessageUIMapperImpl
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.ai.SendRequestUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ChangePasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ClearDataUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithGoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.DeleteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.FetchProvidersForEmailUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleSignInUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleSignOutUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ReAuthenticateUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ResetPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInAnonymouslyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithGoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignOutUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.DeleteChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.GetChatWithIdUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.GetChatsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.InsertChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.InsertChatsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.UpdateChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.UpdateChatsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.DeleteMessagesUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.GetMessagesFromChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.GetMessagesUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.InsertMessageUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.InsertMessagesUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.InsertRemoteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.UpdateRemoteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.FeedbackUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.GetPrivacyPolicyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.IdTokenUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.LanguageUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ReviewUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ThemeUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.authorisation.LoginViewModel
import com.vnteam.talktoai.presentation.viewmodels.authorisation.OnBoardingViewModel
import com.vnteam.talktoai.presentation.viewmodels.authorisation.SignUpViewModel
import com.vnteam.talktoai.presentation.viewmodels.chats.ChatListViewModel
import com.vnteam.talktoai.presentation.viewmodels.chats.ChatViewModel
import com.vnteam.talktoai.presentation.viewmodels.settings.AppViewModel
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsAccountViewModel
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsChatViewModel
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsFeedbackViewModel
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsLanguageViewModel
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsPrivacyPolicyViewModel
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsSignUpViewModel
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsThemeViewModel
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

    single { AIService(get()) }
    single { AuthService(get()) }
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
                /*header("Authorization", "Bearer $Config.AI_API_KEY")
                header("OpenAI-Organization", Config.ORGANIZATION_ID)
                header("OpenAI-Project", Config.PROJECT_ID)*/
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

    // Repositories

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    single<RealDataBaseRepository> { RealDataBaseRepositoryImpl() }

    single<ChatRepository> { ChatRepositoryImpl(get(), get()) }

    single<MessageRepository> { MessageRepositoryImpl(get(), get()) }

    single<AIRepository> { AIRepositoryImpl(get()) }

    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

    single<ChatUIMapper> { ChatUIMapperImpl() }

    single<MessageUIMapper> { MessageUIMapperImpl() }

    // UseCases
    // preferences
    single { FeedbackUseCase(get()) }

    single { LanguageUseCase(get()) }

    single { OnboardingUseCase(get()) }

    single { GetPrivacyPolicyUseCase(get()) }

    single { ReviewUseCase(get()) }

    single { ThemeUseCase(get()) }

    single { UserEmailUseCase(get()) }

    single { IdTokenUseCase(get()) }

    // authorisation
    single { ChangePasswordUseCase(get(), get(), get()) }

    single { ClearDataUseCase() }

    single { CreateUserWithEmailAndPasswordUseCase(get(), get(), get()) }

    single { CreateUserWithGoogleUseCase(get()) }

    single { DeleteUserUseCase(get(), get(), get()) }

    single { FetchProvidersForEmailUseCase(get(), get()) }

    single { GoogleSignInUseCase(get(), get()) }

    single { GoogleSignOutUseCase(get(), get()) }

    single { ReAuthenticateUseCase(get()) }

    single { ResetPasswordUseCase(get(), get()) }

    single { SignInAnonymouslyUseCase(get(), get(), get()) }

    single { SignInWithEmailAndPasswordUseCase(get(), get(), get()) }

    single { SignInWithGoogleUseCase(get()) }

    single { SignOutUseCase(get()) }

    // chats
    single { DeleteChatUseCase(get(), get(), get(), get(), get()) }

    single { GetChatsUseCase(get()) }

    single { GetChatWithIdUseCase(get()) }

    single { InsertChatsUseCase(get()) }

    single { InsertChatUseCase(get(), get(), get(), get()) }

    single { UpdateChatsUseCase(get(), get(), get(), get()) }

    single { UpdateChatUseCase(get(), get(), get(), get()) }

    // messages
    single { DeleteMessagesUseCase(get(), get(), get(), get()) }

    single { GetMessagesFromChatUseCase(get()) }

    single { GetMessagesUseCase(get()) }

    single { InsertMessagesUseCase(get()) }

    single { InsertMessageUseCase(get(), get(), get(), get()) }

    // remote
    single { InsertRemoteUserUseCase(get()) }

    single { UpdateRemoteUserUseCase(get()) }

    // ai
    single { SendRequestUseCase(get()) }

    // ViewModels
    viewModel {
        AppViewModel(get(), get(), get(), get(), get(), get())
    }
    viewModel {
        OnBoardingViewModel(get())
    }
    viewModel {
        LoginViewModel(get(), get(), get(), get())
    }
    viewModel {
        SignUpViewModel(get(), get(), get(), get(), get(), get())
    }
    viewModel {
        ChatListViewModel(get(), get(), get(), get(), get())
    }
    viewModel {
        ChatViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
    viewModel {
        SettingsAccountViewModel(get(), get(), get(), get(), get(), get(), get(), get())
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