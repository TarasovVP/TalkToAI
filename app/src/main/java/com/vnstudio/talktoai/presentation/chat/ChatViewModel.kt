package com.vnstudio.talktoai.presentation.chat

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.data.network.Result
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.ApiResponse
import com.vnstudio.talktoai.domain.usecases.ChatUseCase
import com.vnstudio.talktoai.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(application: Application, private val chatUseCase: ChatUseCase) : BaseViewModel(application) {

    val messagesLiveData = MutableLiveData<List<Message>>()
    val sendRequestLiveData = MutableLiveData<ApiResponse>()

    fun getMessagesFromChat(chatId: Int) {
        showProgress()
        viewModelScope.launch {
            chatUseCase.getMessagesFromChat(chatId).catch {
                hideProgress()
                Log.e("apiTAG", "ChatViewModel getMessagesFromChat catch localizedMessage ${it.localizedMessage}")
            }.collect { result ->
                Log.e("apiTAG", "ChatViewModel getMessagesFromChat collect result $result")
                messagesLiveData.postValue(result)
                hideProgress()
            }
        }
    }

    fun sendRequest(apiRequest: ApiRequest) {
        showProgress()
        viewModelScope.launch {
            chatUseCase.sendRequest(apiRequest).catch {
                hideProgress()
                Log.e("apiTAG", "ChatViewModel sendRequest catch localizedMessage ${it.localizedMessage}")
            }.collect { result ->
                hideProgress()
                when(result) {
                    is Result.Success -> result.data?.let { sendRequestLiveData.postValue(it) }
                    is Result.Failure -> Log.e("apiTAG", "ChatViewModel sendRequest Result.Failure localizedMessage ${result.errorMessage}")
                }
                Log.e("apiTAG", "ChatViewModel sendRequest result $result")
            }
        }
    }

    fun insertMessage(message: Message) {
        showProgress()
        viewModelScope.launch {
            chatUseCase.insertMessage(message)
            hideProgress()
        }
    }
}