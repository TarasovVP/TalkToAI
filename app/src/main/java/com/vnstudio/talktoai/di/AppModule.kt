package com.vnstudio.talktoai.di

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.vnstudio.talktoai.AppDatabase
import com.vnstudio.talktoai.BuildConfig
import com.vnstudio.talktoai.data.database.dao.ChatDao
import com.vnstudio.talktoai.data.database.dao.ChatDaoImpl
import com.vnstudio.talktoai.data.database.dao.MessageDao
import com.vnstudio.talktoai.data.database.dao.MessageDaoImpl
import com.vnstudio.talktoai.data.network.ApiService
import com.vnstudio.talktoai.data.repositoryimpls.AuthRepositoryImpl
import com.vnstudio.talktoai.data.repositoryimpls.ChatRepositoryImpl
import com.vnstudio.talktoai.data.repositoryimpls.DataStoreRepositoryImpl
import com.vnstudio.talktoai.data.repositoryimpls.MessageRepositoryImpl
import com.vnstudio.talktoai.data.repositoryimpls.RealDataBaseRepositoryImpl
import com.vnstudio.talktoai.domain.mappers.MessageUIMapper
import com.vnstudio.talktoai.domain.repositories.AuthRepository
import com.vnstudio.talktoai.domain.repositories.ChatRepository
import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.repositories.MessageRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.ChatUseCase
import com.vnstudio.talktoai.domain.usecases.LoginUseCase
import com.vnstudio.talktoai.domain.usecases.MainUseCase
import com.vnstudio.talktoai.domain.usecases.OnBoardingUseCase
import com.vnstudio.talktoai.domain.usecases.SettingsAccountUseCase
import com.vnstudio.talktoai.domain.usecases.SettingsChatUseCase
import com.vnstudio.talktoai.domain.usecases.SettingsLanguageUseCase
import com.vnstudio.talktoai.domain.usecases.SettingsListUseCase
import com.vnstudio.talktoai.domain.usecases.SettingsPrivacyPolicyUseCase
import com.vnstudio.talktoai.domain.usecases.SettingsSignUpUseCase
import com.vnstudio.talktoai.domain.usecases.SettingsThemeUseCase
import com.vnstudio.talktoai.domain.usecases.SignUpUseCase
import com.vnstudio.talktoai.presentation.mapperimpls.MessageUIMapperImpl
import com.vnstudio.talktoai.presentation.screens.authorization.login.LoginUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.authorization.login.LoginViewModel
import com.vnstudio.talktoai.presentation.screens.authorization.onboarding.OnBoardingUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.authorization.onboarding.OnBoardingViewModel
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpViewModel
import com.vnstudio.talktoai.presentation.screens.chat.ChatUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.chat.ChatViewModel
import com.vnstudio.talktoai.presentation.screens.main.MainUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.main.MainViewModel
import com.vnstudio.talktoai.presentation.screens.settings.settings_account.SettingsAccountUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.settings.settings_account.SettingsAccountViewModel
import com.vnstudio.talktoai.presentation.screens.settings.settings_chat.SettingsChatUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.settings.settings_chat.SettingsChatViewModel
import com.vnstudio.talktoai.presentation.screens.settings.settings_feedback.SettingsFeedbackUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.settings.settings_feedback.SettingsFeedbackViewModel
import com.vnstudio.talktoai.presentation.screens.settings.settings_language.SettingsLanguageUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.settings.settings_language.SettingsLanguageViewModel
import com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyViewModel
import com.vnstudio.talktoai.presentation.screens.settings.settings_sign_up.SettingSignUpViewModel
import com.vnstudio.talktoai.presentation.screens.settings.settings_sign_up.SettingsSignUpUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.settings.settings_theme.SettingsThemeUseCaseImpl
import com.vnstudio.talktoai.presentation.screens.settings.settings_theme.SettingsThemeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(produceFile = {
            androidContext().preferencesDataStoreFile(androidContext().packageName)
        })
    }
    single<DataStoreRepository> { DataStoreRepositoryImpl(get()) }
    single { BuildConfig.BASE_URL }
    single { ApiService(get<String>(), get()) }
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(DefaultRequest) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
                header("OpenAI-Organization", BuildConfig.ORGANIZATION_ID)
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.e("Logger Ktor =>", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
    single {
        val sqlDriver = AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = androidContext(),
            name = "person.db"
        )
        AppDatabase(sqlDriver)
    }
    single { FirebaseAuth.getInstance() }
    single {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.SERVER_CLIENT_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(androidContext(), gso)
    }
    single { FirebaseDatabase.getInstance(BuildConfig.REALTIME_DATABASE) }
    single<RealDataBaseRepository> {
        RealDataBaseRepositoryImpl(
            firebaseDatabase = get(),
            firebaseAuth = get()
        )
    }
    single<ChatDao> { ChatDaoImpl(get<AppDatabase>().appDatabaseQueries) }
    single<MessageDao> { MessageDaoImpl(get<AppDatabase>().appDatabaseQueries) }
    single<ChatRepository> {
        ChatRepositoryImpl(
            chatDao = get()
        )
    }
    single<MessageRepository> {
        MessageRepositoryImpl(
            messageDao = get(),
            apiService = get()
        )
    }
    single<OnBoardingUseCase> {
        OnBoardingUseCaseImpl(
            dataStoreRepository = get()
        )
    }
    single<AuthRepository> {
        AuthRepositoryImpl(
            firebaseAuth = get(),
            googleSignInClient = get()
        )
    }
    single<LoginUseCase> {
        LoginUseCaseImpl(
            authRepository = get()
        )
    }
    single<SignUpUseCase> {
        SignUpUseCaseImpl(
            authRepository = get(),
            realDataBaseRepository = get()
        )
    }
    single<MessageUIMapper> {
        MessageUIMapperImpl()
    }
    single<ChatUseCase> {
        ChatUseCaseImpl(
            chatRepository = get(),
            messageRepository = get(),
            authRepository = get(),
            realDataBaseRepository = get(),
            messageUIMapper = get()
        )
    }
    single<MainUseCase> {
        MainUseCaseImpl(
            authRepository = get(),
            dataStoreRepository = get(),
            realDataBaseRepository = get(),
            chatRepository = get(),
            messageRepository = get()
        )
    }
    single<SettingsListUseCase> {
        SettingsFeedbackUseCaseImpl(
            authRepository = get(),
            realDataBaseRepository = get()
        )
    }
    single<SettingsChatUseCase> {
        SettingsChatUseCaseImpl(
            firebaseAuth = get(),
            dataStoreRepository = get(),
            realDataBaseRepository = get()
        )
    }
    single<SettingsAccountUseCase> {
        SettingsAccountUseCaseImpl(
            authRepository = get(),
            dataStoreRepository = get(),
            realDataBaseRepository = get()
        )
    }
    single<SettingsSignUpUseCase> {
        SettingsSignUpUseCaseImpl(
            chatRepository = get(),
            messageRepository = get(),
            authRepository = get(),
            realDataBaseRepository = get()
        )
    }
    single<SettingsLanguageUseCase> {
        SettingsLanguageUseCaseImpl(
            dataStoreRepository = get()
        )
    }
    single<SettingsThemeUseCase> {
        SettingsThemeUseCaseImpl(
            dataStoreRepository = get()
        )
    }
    single<SettingsPrivacyPolicyUseCase> {
        SettingsPrivacyPolicyUseCaseImpl(
            dataStoreRepository = get(),
            realDataBaseRepository = get()
        )
    }
    viewModel {
        LoginViewModel(
            application = androidApplication(),
            loginUseCase = get(),
            googleSignInClient = get()
        )
    }
    viewModel {
        OnBoardingViewModel(
            application = androidApplication(),
            onBoardingUseCase = get()
        )
    }
    viewModel {
        SignUpViewModel(
            application = androidApplication(),
            signUpUseCase = get(),
            googleSignInClient = get()
        )
    }
    viewModel {
        ChatViewModel(
            application = androidApplication(),
            chatUseCase = get()
        )
    }
    viewModel {
        MainViewModel(
            application = androidApplication(),
            mainUseCase = get()
        )
    }
    viewModel {
        SettingsAccountViewModel(
            application = androidApplication(),
            settingsAccountUseCase = get(),
            googleSignInClient = get()
        )
    }
    viewModel {
        SettingsChatViewModel(
            application = androidApplication(),
            settingsChatUseCase = get()
        )
    }
    viewModel {
        SettingsFeedbackViewModel(
            application = androidApplication(),
            settingsListUseCase = get()
        )
    }
    viewModel {
        SettingsLanguageViewModel(
            application = androidApplication(),
            settingsLanguageUseCase = get()
        )
    }
    viewModel {
        SettingsPrivacyPolicyViewModel(
            application = androidApplication(),
            settingsPrivacyPolicyUseCase = get()
        )
    }
    viewModel {
        SettingSignUpViewModel(
            application = androidApplication(),
            settingsSignUpUseCase = get(),
            googleSignInClient = get()
        )
    }
    viewModel {
        SettingsThemeViewModel(
            application = androidApplication(),
            settingsThemeUseCase = get()
        )
    }
}