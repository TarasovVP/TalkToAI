package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase

class OnBoardingViewModel(
    private val onboardingUseCase: OnboardingUseCase,
) : BaseViewModel() {

    fun setOnBoardingSeen() {
        launchWithErrorHandling {
            println("appTAG OnBoardingViewModel setOnBoardingSeen")
            onboardingUseCase.setOnBoardingSeen(true)
        }
    }
}