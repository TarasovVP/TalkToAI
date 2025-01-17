package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.preferences.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.preferences.UserLoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsChatViewModel(
    private val onboardingUseCase: OnboardingUseCase,
    private val userLoginUseCase: UserLoginUseCase
) : BaseViewModel() {

    val chatSettingsLiveData = MutableStateFlow(false)

    fun clearUserData() {
        launchWithErrorHandling {
            onboardingUseCase.set(false)
            userLoginUseCase.set(String.EMPTY)
        }
    }
}