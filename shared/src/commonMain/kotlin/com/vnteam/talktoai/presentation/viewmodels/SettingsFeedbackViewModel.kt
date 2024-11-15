package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.usecase.SettingsListUseCase
import kotlinx.coroutines.flow.MutableStateFlow


class SettingsFeedbackViewModel(
    private val settingsListUseCase: SettingsListUseCase,
) : BaseViewModel() {

    val successFeedbackLiveData = MutableStateFlow(false)

    fun currentUserEmail(): String {
        return settingsListUseCase.currentUserEmail()
    }

    fun insertFeedback(feedback: Feedback) {
        /*if (application.isNetworkAvailable()) {
            showProgress()
            settingsListUseCase.insertFeedback(feedback) { result ->
                when (result) {
                    is NetworkResult.Success -> successFeedbackLiveData.value = true
                    is NetworkResult.Failure -> exceptionLiveData.value = result.errorMessage.orEmpty()
                }
            }
            hideProgress()
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }*/
    }
}