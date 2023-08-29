package com.vnstudio.talktoai.presentation.screens.onboarding.onboarding

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.domain.usecases.OnBoardingUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    application: Application,
    private val onBoardingUseCase: OnBoardingUseCase
) : BaseViewModel(application) {

    val onBoardingSeenLiveData = MutableLiveData<Unit>()

    fun setOnBoardingSeen() {
        launch {
            onBoardingUseCase.setOnBoardingSeen(true)
            onBoardingSeenLiveData.postValue(Unit)
        }
    }
}