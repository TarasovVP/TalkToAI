package com.vnstudio.talktoai.presentation.screens.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.R
import kotlinx.coroutines.*

open class BaseViewModel(private val application: Application) : AndroidViewModel(application) {

    val exceptionLiveData = MutableLiveData<String>()
    val isProgressProcessLiveData = MutableLiveData<Boolean>()

    fun showProgress() {
        isProgressProcessLiveData.postValue(true)
    }

    fun hideProgress() {
        isProgressProcessLiveData.postValue(false)
    }

    fun checkNetworkAvailable(networkAvailableResult: () -> Unit) {
        if (application.isNetworkAvailable()) {
            networkAvailableResult.invoke()
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
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
        exceptionLiveData.postValue(throwable.localizedMessage)
    }
}