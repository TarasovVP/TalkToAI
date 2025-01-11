package com.vnteam.talktoai.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val _exceptionMessage = MutableStateFlow(String.EMPTY)
    val exceptionMessage = _exceptionMessage.asStateFlow()

    private val _progressVisibilityState = MutableStateFlow(false)
    val progressVisibilityState = _progressVisibilityState.asStateFlow()

    private val _innerProgress = MutableStateFlow(false)
    val innerProgress = _innerProgress.asStateFlow()

    fun showProgress() {
        _innerProgress.value = true
        println("appTAG BaseViewModel showProgress: progressVisibilityState ${progressVisibilityState.value}")
    }

    fun hideProgress() {
        _innerProgress.value = false
        println("appTAG BaseViewModel hideProgress: progressVisibilityState ${progressVisibilityState.value}")
    }

    fun showMessage(message: String) {
        _exceptionMessage.value = message
        viewModelScope.launch {
            delay(Constants.APP_MESSAGE_DELAY)
            _exceptionMessage.value = String.EMPTY
        }
    }

    protected fun <T> launchWithNetworkCheck(
        networkState: NetworkState? = null,
        block: suspend CoroutineScope.() -> Flow<NetworkResult<T>>
    ): Job {
        networkState?.let {
            if (it.isNetworkAvailable().not()) {
                onError(Exception(Constants.APP_NETWORK_UNAVAILABLE_REPEAT))
                return launchWithErrorHandling { }
            }
        }
        return launchWithResultHandling(block)
    }

    protected fun <T> launchWithResultHandling(
        block: suspend CoroutineScope.() -> Flow<NetworkResult<T>>
    ): Job = launchWithErrorHandling {
        block().collect { result ->
            println("appTAG BaseViewModel launchWithConditionsTest: result $result")
            when (result) {
                is NetworkResult.Success -> hideProgress()
                is NetworkResult.Failure -> onError(Exception(result.errorMessage))
                is NetworkResult.Loading -> showProgress()
            }
        }
    }

    protected fun launchWithErrorHandling(
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
        onError(exception)
    }) {
        try {
            block()
        } catch (e: Exception) {
            onError(e)
        }
    }

    protected open fun onError(throwable: Throwable) {
        throwable.printStackTrace()
        showMessage(throwable.message.orEmpty())
        hideProgress()
    }
}