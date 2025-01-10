package com.vnteam.talktoai.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {

    val _exceptionMessage = MutableStateFlow(String.EMPTY)
    val exceptionMessage = _exceptionMessage.asStateFlow()

    val _progressVisibilityState = MutableStateFlow(false)
    val progressVisibilityState = _progressVisibilityState.asStateFlow()

    fun showProgress() {
        _progressVisibilityState.value = true
        println("appTAG BaseViewModel showProgress: progressVisibilityState ${progressVisibilityState.value}")
    }

    fun hideProgress() {
        _progressVisibilityState.value = false
        println("appTAG BaseViewModel hideProgress: progressVisibilityState ${progressVisibilityState.value}")
    }

    fun showMessage(message: String) {
        _exceptionMessage.value = message
        viewModelScope.launch {
            delay(Constants.APP_MESSAGE_DELAY)
            _exceptionMessage.value = String.EMPTY
        }
    }

    protected fun launchWithConditions(
        networkState: NetworkState? = null,
        isProgressNeeded: Boolean = false,
        onError: (Throwable) -> Any? = ::onError,
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
        onError(exception)
    }) {
        if (isProgressNeeded) {
            showProgress()
        }
        delay(2000)

        networkState?.let {
            if (it.isNetworkAvailable().not()) {
                throw Exception(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
            }
        }

        withContext(Dispatchers.Unconfined) {
            block()
        }
        if (isProgressNeeded) {
            hideProgress()
        }
    }

    protected fun <T> launchWithConditionsTest(
        networkState: NetworkState? = null,
        isProgressNeeded: Boolean = false,
        block: suspend CoroutineScope.() -> Flow<NetworkResult<T>>,
        onSuccess: (T?) -> Unit = {},
        onError: (Throwable) -> Any? = ::onError,
    ): Job = viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
        onError(exception)
    }) {
        if (isProgressNeeded) {
            showProgress()
        }
        delay(2000)

        networkState?.let {
            if (it.isNetworkAvailable().not()) {
                throw Exception(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
            }
        }

        block().collect { result ->
            when (result) {
                is NetworkResult.Success -> onSuccess(result.data)
                is NetworkResult.Failure -> throw Exception(result.errorMessage)
            }
        }
        if (isProgressNeeded) {
            hideProgress()
        }
    }

    protected open fun onError(throwable: Throwable) {
        throwable.printStackTrace()
        showMessage(throwable.message.orEmpty())
        hideProgress()
    }
}