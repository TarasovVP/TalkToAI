package com.vnteam.talktoai.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    }

    fun hideProgress() {
        _progressVisibilityState.value = false
    }

    fun showMessage(message: String) {
        launch {
            _exceptionMessage.value = message
            delay(4000)
            _exceptionMessage.value = String.EMPTY
        }
    }

    protected fun launch(
        networkState: NetworkState? = null,
        onError: (Throwable, suspend CoroutineScope.() -> Unit) -> Any? = ::onError,
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
        onError(exception, block)
    }) {
        networkState?.let {
            if (it.isNetworkAvailable().not()) {
                throw Exception(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
            }
        }
        withContext(Dispatchers.Unconfined) {
            block()
        }
    }

    protected open fun onError(throwable: Throwable, block: suspend CoroutineScope.() -> Unit) {
        hideProgress()
        throwable.printStackTrace()
        _exceptionMessage.value = throwable.message.orEmpty()
    }
}