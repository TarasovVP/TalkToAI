package com.vnteam.talktoai.di

import com.vnteam.talktoai.data.repositoryimpl.PreferencesRepositoryImpl
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiModelUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ApiKeyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.TemperatureUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.IdTokenUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.LanguageUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ReviewUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ThemeUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import org.koin.dsl.module

val settingsModule = module {

    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

    single { LanguageUseCase(get()) }

    single { OnboardingUseCase(get()) }

    single { ReviewUseCase(get()) }

    single { ThemeUseCase(get()) }

    single { UserEmailUseCase(get()) }

    single { IdTokenUseCase(get()) }

    single { AiModelUseCase(get()) }

    single { ApiKeyUseCase(get()) }

    single { TemperatureUseCase(get()) }
}
