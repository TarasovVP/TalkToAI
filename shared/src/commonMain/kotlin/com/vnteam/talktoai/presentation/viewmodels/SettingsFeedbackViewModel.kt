package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.Constants.APP_NETWORK_UNAVAILABLE_REPEAT
import com.vnteam.talktoai.data.network.NetworkResult
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
        if (networkState.isNetworkAvailable()) {
            showProgress()
            settingsListUseCase.insertFeedback(feedback) { result ->
                when (result) {
                    is NetworkResult.Success -> successFeedbackLiveData.value = true
                    is NetworkResult.Failure -> _exceptionMessage.value =
                        result.errorMessage.orEmpty()
                }
            }
            hideProgress()
        } else {
            _exceptionMessage.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }
}