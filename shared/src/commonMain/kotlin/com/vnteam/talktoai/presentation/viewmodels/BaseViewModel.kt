package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.CommonExtensions.EMPTY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {

    val exceptionLiveData = MutableStateFlow<String?>(String.EMPTY)
    val progressVisibilityLiveData = MutableStateFlow(false)

    fun showProgress() {
        progressVisibilityLiveData.value = true
    }

    fun hideProgress() {
        progressVisibilityLiveData.value = false
    }

    fun checkNetworkAvailable(networkAvailableResult: () -> Unit) {
        /*if (application.isNetworkAvailable()) {
            networkAvailableResult.invoke()
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }*/
    }

    protected fun launch(
        onError: (Throwable, suspend CoroutineScope.() -> Unit) -> Any? = ::onError,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
        onError(exception, block)
    }) {
        withContext(Dispatchers.Unconfined) {
            block()
        }
    }

    protected open fun onError(throwable: Throwable, block: suspend CoroutineScope.() -> Unit) {
        hideProgress()
        throwable.printStackTrace()
        exceptionLiveData.value = throwable.message
    }
}