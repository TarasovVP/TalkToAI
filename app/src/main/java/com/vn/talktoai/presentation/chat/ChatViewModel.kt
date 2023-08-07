package com.vn.talktoai.presentation.chat

import android.app.Application
import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.ApiResponse
import com.vn.talktoai.domain.usecases.ChatUseCase
import com.vn.talktoai.presentation.BaseViewModel
import com.vn.talktoai.data.Result
import com.vn.talktoai.domain.models.Choice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(application: Application, private val chatUseCase: ChatUseCase) : BaseViewModel(application) {

    val completionLiveData = MutableLiveData<ApiResponse>()

    fun getCompletions(apiRequest: ApiRequest) {
        showProgress()
        viewModelScope.launch {
            chatUseCase.getCompletions(apiRequest).catch {
                hideProgress()
                Log.e("apiTAG", "ChatViewModel getCompletions catch localizedMessage ${it.localizedMessage}")
            }.collect { result ->
                when(result) {
                    is Result.Success -> result.data?.let { completionLiveData.postValue(it) }
                    is Result.Failure -> Log.e("apiTAG", "ChatViewModel getCompletions Result.Failure localizedMessage ${result.errorMessage}")
                }
                hideProgress()
                Log.e("apiTAG", "ChatViewModel getCompletions result $result")
            }
        }
    }
}