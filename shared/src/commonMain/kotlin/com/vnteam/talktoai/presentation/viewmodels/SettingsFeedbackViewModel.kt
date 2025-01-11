package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.usecase.SettingsListUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow


class SettingsFeedbackViewModel(
    private val settingsListUseCase: SettingsListUseCase,
    private val networkState: NetworkState
) : BaseViewModel() {

    val successFeedbackLiveData = MutableStateFlow(false)

    fun currentUserEmail(): String {
        return settingsListUseCase.currentUserEmail()
    }

    fun insertFeedback(feedback: Feedback) {
        launchWithNetworkCheck(networkState) {
            settingsListUseCase.insertFeedback(feedback).onSuccess {
                successFeedbackLiveData.value = true
            }
        }
    }
}