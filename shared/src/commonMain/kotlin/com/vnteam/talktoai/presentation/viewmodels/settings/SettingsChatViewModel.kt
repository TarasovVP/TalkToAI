package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsChatViewModel(
    private val onboardingUseCase: OnboardingUseCase,
    private val userEmailUseCase: UserEmailUseCase,
) : BaseViewModel() {

    val chatSettingsLiveData = MutableStateFlow(false)

    fun clearUserData() {
        launchWithErrorHandling {
            onboardingUseCase.set(false)
            userEmailUseCase.set(String.EMPTY)
        }
    }
}