package com.vnteam.talktoai.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.CommonExtensions.isNotNull
import com.vnteam.talktoai.CommonExtensions.isNotTrue
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
        println("appTAG BaseViewModel showProgress: progressVisibilityState ${progressVisibilityState.value}")
    }

    fun hideProgress() {
        _progressVisibilityState.value = false
        println("appTAG BaseViewModel hideProgress: progressVisibilityState ${progressVisibilityState.value}")
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
        isProgressNeeded: Boolean = false,
        onError: (Throwable) -> Any? = ::onError,
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
        onError(exception)
    }) {
        println("appTAG BaseViewModel launch start networkState $networkState isProgressNeeded $isProgressNeeded")
        if (isProgressNeeded) {
            println("appTAG BaseViewModel launch showProgress() progressVisibilityState ${progressVisibilityState.value}")
            showProgress()
        }
        delay(2000)

        networkState?.let {
            if (it.isNetworkAvailable().not()) {
                onError(Exception(Constants.APP_NETWORK_UNAVAILABLE_REPEAT))
                println("appTAG BaseViewModel launch networkState.isNetworkAvailable().not()")
            }
        }
        val isNetworkNeededAndAvailable = isProgressNeeded && networkState.isNotNull() && networkState?.isNetworkAvailable().isNotTrue()
        println("appTAG BaseViewModel isNetworkNeededAndAvailable $isNetworkNeededAndAvailable")
        if (isNetworkNeededAndAvailable.not()) {
            withContext(Dispatchers.Unconfined) {
                println("appTAG BaseViewModel launchblock() progressVisibilityState ${progressVisibilityState.value}")
                block()
            }
            if (isProgressNeeded) {
                hideProgress()
            }
        }
    }

    protected open fun onError(throwable: Throwable) {
        println("appTAG BaseViewModel onError: progressVisibilityState ${progressVisibilityState.value}")
        hideProgress()
        throwable.printStackTrace()
        showMessage(throwable.message.orEmpty())
    }
}