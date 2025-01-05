package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.domain.usecase.OnBoardingUseCase

class OnBoardingViewModel(
    private val onBoardingUseCase: OnBoardingUseCase,
) : BaseViewModel() {


    fun setOnBoardingSeen() {
        launch {
            println("appTAG OnBoardingViewModel setOnBoardingSeen")
            onBoardingUseCase.setOnBoardingSeen(true)
        }
    }
}