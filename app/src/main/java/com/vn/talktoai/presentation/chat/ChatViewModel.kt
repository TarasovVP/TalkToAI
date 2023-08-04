package com.vn.talktoai.presentation.chat

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import com.vn.talktoai.domain.usecases.ChatUseCase
import com.vn.talktoai.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(application: Application, private val chatUseCase: ChatUseCase) : BaseViewModel(application) {

    val completionLiveData = MutableLiveData<ApiResponse>()

    fun getCompletions(apiRequest: ApiRequest) {
        viewModelScope.launch {
            chatUseCase.getCompletions(apiRequest).catch {
                Log.e("apiTAG", "MainActivity onResponse catch localizedMessage ${it.localizedMessage}")
            }.collect { apiResponse ->
                //completionLiveData.postValue(apiResponse)
                Log.e("apiTAG", "MainActivity onResponse isSuccessful apiResponse $apiResponse")
            }
        }
    }
}