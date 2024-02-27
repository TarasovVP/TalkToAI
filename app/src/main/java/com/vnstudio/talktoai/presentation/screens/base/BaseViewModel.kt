package com.vnstudio.talktoai.presentation.screens.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

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
            exceptionLiveData.value = application.getString(R.string.app_network_unavailable_repeat)
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
        Log.e("exceptionTAG", "BaseViewModel onError throwable ${throwable.localizedMessage}")
        exceptionLiveData.value = throwable.localizedMessage
    }
}