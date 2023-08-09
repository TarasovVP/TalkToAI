package com.vn.talktoai.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application) : BaseViewModel(application) {

    val chatsLiveData = MutableLiveData<List<String>>()

    fun getChats() {
        chatsLiveData.postValue(listOf("Chat1", "Chat2"))
    }
}