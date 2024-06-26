package com.vn.talktoai.presentation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vn.talktoai.data.database.db_entities.Chat
import com.vn.talktoai.domain.usecases.MainUseCase
import com.vn.talktoai.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainUseCase: MainUseCase, application: Application) : BaseViewModel(application) {

    val chatsLiveData = MutableLiveData<List<Chat>>()

    fun insertChat(chat: Chat) {
        showProgress()
        viewModelScope.launch {
            mainUseCase.insertChat(chat)
        }
    }

    fun getChats() {
        showProgress()
        viewModelScope.launch {
            mainUseCase.getChats().catch {
                hideProgress()
                Log.e("apiTAG", "MainViewModel getChats catch localizedMessage ${it.localizedMessage}")
            }.collect { result ->
                chatsLiveData.postValue(result)
                hideProgress()
                Log.e("apiTAG", "MainViewModel getChats result $result")
            }
        }
    }

    fun updateChat(chat: Chat) {
        showProgress()
        viewModelScope.launch {
            mainUseCase.updateChat(chat)
        }
    }

    fun deleteChat(chat: Chat) {
        showProgress()
        viewModelScope.launch {
            mainUseCase.deleteChat(chat)
        }
    }
}