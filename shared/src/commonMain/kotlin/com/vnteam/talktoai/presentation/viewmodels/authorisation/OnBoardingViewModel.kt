package com.vnteam.talktoai.presentation.viewmodels.authorisation

import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel

class OnBoardingViewModel(
    private val onboardingUseCase: OnboardingUseCase,
) : BaseViewModel() {

    fun setOnBoardingSeen() {
        launchWithErrorHandling {
            println("appTAG OnBoardingViewModel setOnBoardingSeen")
            onboardingUseCase.set(true)
        }
    }
}