package com.vnstudio.talktoai.presentation.screens.authorization.onboarding

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.domain.usecases.OnBoardingUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel



class OnBoardingViewModel(
    application: Application,
    private val onBoardingUseCase: OnBoardingUseCase,
) : BaseViewModel(application) {

    val onBoardingSeenLiveData = MutableLiveData<Unit>()

    fun setOnBoardingSeen() {
        launch {
            onBoardingUseCase.setOnBoardingSeen(true)
            onBoardingSeenLiveData.postValue(Unit)
        }
    }
}