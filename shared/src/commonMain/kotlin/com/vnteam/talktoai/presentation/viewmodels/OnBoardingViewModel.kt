package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.domain.usecase.OnBoardingUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class OnBoardingViewModel(
    private val onBoardingUseCase: OnBoardingUseCase,
) : BaseViewModel() {

    val onBoardingSeenLiveData = MutableStateFlow(false)

    fun setOnBoardingSeen() {
        launch {
            onBoardingUseCase.setOnBoardingSeen(true)
            onBoardingSeenLiveData.value = true
        }
    }
}