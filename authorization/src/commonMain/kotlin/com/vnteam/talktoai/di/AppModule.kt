package com.vnteam.talktoai.di

import com.vnteam.talktoai.data.network.auth.AuthHttpClient
import com.vnteam.talktoai.data.network.auth.AuthService
import com.vnteam.talktoai.data.repositoryimpl.AuthRepositoryImpl
import com.vnteam.talktoai.domain.repositories.AuthRepository
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
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {

    single { AuthService(get()) }
    single {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single { AuthHttpClient(get()) }


    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // authorisation
    single { ChangePasswordUseCase(get()) }

    single { ClearDataUseCase() }

    single { CreateUserWithEmailAndPasswordUseCase(get()) }

    single { CreateUserWithGoogleUseCase(get()) }

    single { DeleteUserUseCase(get()) }

    single { FetchProvidersForEmailUseCase(get()) }

    single { GoogleSignInUseCase(get()) }

    single { GoogleSignOutUseCase(get()) }

    single { ReAuthenticateUseCase(get()) }

    single { ResetPasswordUseCase(get()) }

    single { SignInAnonymouslyUseCase(get()) }

    single { SignInWithEmailAndPasswordUseCase(get()) }

    single { SignInWithGoogleUseCase(get()) }

    single { SignOutUseCase(get()) }
}