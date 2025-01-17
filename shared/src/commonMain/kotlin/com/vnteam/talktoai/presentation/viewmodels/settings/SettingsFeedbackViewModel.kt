package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.FeedbackUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserLoginUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsFeedbackViewModel(
    private val userLoginUseCase: UserLoginUseCase,
    private val feedbackUseCase: FeedbackUseCase,
    private val networkState: NetworkState
) : BaseViewModel() {

    private val _currentUserLogin = MutableStateFlow(String.EMPTY)
    val userLogin = _currentUserLogin.asStateFlow()

    private val _previousFeedbacks = MutableStateFlow<List<Feedback>>(emptyList())
    val previousFeedbacks = _currentUserLogin.asStateFlow()

    private val _successFeedback = MutableStateFlow(false)
    val successFeedback = _successFeedback.asStateFlow()

    fun getCurrentUserLogin() {
        userLoginUseCase.get().onSuccess {
            _currentUserLogin.value = it.orEmpty()
        }
    }

    fun getFeedbacks() {
        launchWithNetworkCheck(networkState) {
            feedbackUseCase.get().onSuccess {
                _previousFeedbacks.value = it.orEmpty()
            }
        }
    }

    fun insertFeedback(feedback: Feedback) {
        /*launchWithNetworkCheck(networkState) {
            feedbackUseCase.set(feedback).onSuccess {
                _successFeedback.value = true
            }
        }*/
    }
}