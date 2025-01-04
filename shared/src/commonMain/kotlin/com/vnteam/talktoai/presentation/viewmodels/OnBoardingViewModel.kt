package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.domain.usecase.OnBoardingUseCase

class OnBoardingViewModel(
    private val onBoardingUseCase: OnBoardingUseCase,
) : BaseViewModel() {


    fun setOnBoardingSeen() {
        launch {
            onBoardingUseCase.setOnBoardingSeen(true)
        }
    }
}