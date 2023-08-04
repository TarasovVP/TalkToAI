package com.vn.talktoai.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val exceptionLiveData = MutableLiveData<String>()
    val isProgressProcessLiveData = MutableLiveData<Boolean>()

    fun showProgress() {
        isProgressProcessLiveData.postValue(true)
    }

    fun hideProgress() {
        isProgressProcessLiveData.postValue(false)
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
        exceptionLiveData.postValue(throwable.localizedMessage)
    }
}