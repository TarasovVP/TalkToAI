package com.vnstudio.talktoai.presentation.screens.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.infrastructure.Constants.APP_NETWORK_UNAVAILABLE_REPEAT
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel(private val application: Application) : AndroidViewModel(application) {

    val exceptionLiveData = MutableStateFlow<String?>(String.EMPTY)
    val progressVisibilityLiveData = MutableStateFlow(false)

    fun showProgress() {
        progressVisibilityLiveData.value = true
    }

    fun hideProgress() {
        progressVisibilityLiveData.value = false
    }

    fun checkNetworkAvailable(networkAvailableResult: () -> Unit) {
        if (application.isNetworkAvailable()) {
            networkAvailableResult.invoke()
        } else {
            exceptionLiveData.value = APP_NETWORK_UNAVAILABLE_REPEAT
        }
    }

    protected fun launch(
        onError: (Throwable, suspend CoroutineScope.() -> Unit) -> Any? = ::onError,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
        onError(exception, block)
    }) {
        withContext(Dispatchers.IO) {
            block()
        }
    }

    protected open fun onError(throwable: Throwable, block: suspend CoroutineScope.() -> Unit) {
        hideProgress()
        throwable.printStackTrace()
        exceptionLiveData.value = throwable.localizedMessage
    }
}