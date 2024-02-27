package com.vnstudio.talktoai.presentation.screens.authorization.onboarding

import android.app.Application
import com.vnstudio.talktoai.domain.usecases.OnBoardingUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class OnBoardingViewModel(
    application: Application,
    private val onBoardingUseCase: OnBoardingUseCase,
) : BaseViewModel(application) {

    val onBoardingSeenLiveData = MutableStateFlow(Unit)

    fun setOnBoardingSeen() {
        launch {
            onBoardingUseCase.setOnBoardingSeen(true)
            onBoardingSeenLiveData.value = Unit
        }
    }
}