package com.vnstudio.talktoai.presentation.screens.settings.settings_feedback

import android.app.Application
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsListUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class SettingsFeedbackViewModel(
    private val application: Application,
    private val settingsListUseCase: SettingsListUseCase,
) : BaseViewModel(application) {

    val successFeedbackLiveData = MutableStateFlow(Unit)

    fun currentUserEmail(): String {
        return settingsListUseCase.currentUserEmail()
    }

    fun insertFeedback(feedback: Feedback) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsListUseCase.insertFeedback(feedback) { result ->
                when (result) {
                    is Result.Success -> successFeedbackLiveData.value = Unit
                    is Result.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
            }
            hideProgress()
        } else {
            exceptionLiveData.value = application.getString(R.string.app_network_unavailable_repeat)
        }
    }
}