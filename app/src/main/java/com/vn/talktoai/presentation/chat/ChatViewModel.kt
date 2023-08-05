package com.vn.talktoai.presentation.chat

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import com.vn.talktoai.domain.usecases.ChatUseCase
import com.vn.talktoai.presentation.BaseViewModel
import com.vn.talktoai.data.Result
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
                Log.e("apiTAG", "ChatViewModel getCompletions catch localizedMessage ${it.localizedMessage}")
            }.collect { result ->
                when(result) {
                    is Result.Success -> result.data?.let { completionLiveData.postValue(it) }
                    is Result.Failure -> Log.e("apiTAG", "ChatViewModel getCompletions Result.Failure localizedMessage ${result.errorMessage}")
                }
                Log.e("apiTAG", "ChatViewModel getCompletions result $result")
            }
        }
    }
}