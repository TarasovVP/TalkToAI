package com.vn.talktoai.presentation.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import com.vn.talktoai.domain.usecases.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatUseCase: ChatUseCase) : ViewModel() {

    val completionLiveData = MutableLiveData<ApiResponse>()

    fun getCompletions(apiRequest: ApiRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            chatUseCase.getCompletions(apiRequest).collect { apiResponse ->
                completionLiveData.postValue(apiResponse)
                Log.e("apiTAG", "MainActivity onResponse isSuccessful apiResponse $apiResponse")
            }
        }
    }
}