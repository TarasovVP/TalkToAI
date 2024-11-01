package com.vnstudio.talktoai.presentation.screens.settings.settings_feedback

import android.app.Application
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable

import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsListUseCase
import com.vnstudio.talktoai.infrastructure.Constants.APP_NETWORK_UNAVAILABLE_REPEAT
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class SettingsFeedbackViewModel(
    private val application: Application,
    private val settingsListUseCase: SettingsListUseCase,
) : BaseViewModel(application) {

    val successFeedbackLiveData = MutableStateFlow(false)

    fun currentUserEmail(): String {
        return settingsListUseCase.currentUserEmail()
    }

    fun insertFeedback(feedback: Feedback) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsListUseCase.insertFeedback(feedback) { result ->
                when (result) {
                    is Result.Success -> successFeedbackLiveData.value = true
                    is Result.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
            }
            hideProgress()
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }
}