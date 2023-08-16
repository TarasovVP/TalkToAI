package com.vnstudio.talktoai.presentation.chat

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.data.network.Result
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.usecases.ChatUseCase
import com.vnstudio.talktoai.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(application: Application, private val chatUseCase: ChatUseCase) : BaseViewModel(application) {

    val currentChatLiveData = MutableLiveData<Chat>()
    val messagesLiveData = MutableLiveData<List<Message>>()

    fun getCurrentChat() {
        viewModelScope.launch {
            chatUseCase.getCurrentChat().catch {
                hideProgress()
                Log.e("apiTAG", "ChatViewModel getCurrentChat catch localizedMessage ${it.localizedMessage}")
            }.collect { result ->
                Log.e("apiTAG", "ChatViewModel getCurrentChat collect result $result isProgressProcessLiveData ${isProgressProcessLiveData.value}")
                currentChatLiveData.value = result
                result?.chatId?.let { getMessagesFromChat(it) }
            }
        }
    }

    private fun getMessagesFromChat(chatId: Int) {
        viewModelScope.launch {
            chatUseCase.getMessagesFromChat(chatId).catch {
                hideProgress()
                Log.e("apiTAG", "ChatViewModel getMessagesFromChat catch localizedMessage ${it.localizedMessage} isProgressProcessLiveData ${isProgressProcessLiveData.value}")
            }.collect { result ->
                Log.e("apiTAG", "ChatViewModel getMessagesFromChat collect result.size ${result.size} chatId $chatId isProgressProcessLiveData ${isProgressProcessLiveData.value}")
                messagesLiveData.postValue(result)

            }
        }
    }

    fun sendRequest(chatId: Int, apiRequest: ApiRequest) {
        showProgress()
        viewModelScope.launch {
            chatUseCase.sendRequest(apiRequest).catch {
                hideProgress()
                Log.e("apiTAG", "ChatViewModel sendRequest catch localizedMessage ${it.localizedMessage} isProgressProcessLiveData ${isProgressProcessLiveData.value}")
            }.collect { result ->
                when(result) {
                    is Result.Success -> result.data?.let { apiResponse ->
                        messagesLiveData.value?.lastOrNull()?.apply {
                            author = apiResponse.model.orEmpty()
                            message = apiResponse.choices?.firstOrNull()?.message?.content.orEmpty()
                            createdAt = apiResponse.created?.toLongOrNull() ?: 0
                        }?.let { updateMessage(it) }
                    }
                    is Result.Failure -> Log.e("apiTAG", "ChatViewModel sendRequest Result.Failure localizedMessage ${result.errorMessage}  isProgressProcessLiveData ${isProgressProcessLiveData.value}")
                }
                hideProgress()
                Log.e("apiTAG", "ChatViewModel sendRequest result $result  isProgressProcessLiveData ${isProgressProcessLiveData.value}")
            }
        }
    }

    fun insertMessage(message: Message) {
        viewModelScope.launch {
            chatUseCase.insertMessage(message)
            Log.e("apiTAG", "ChatViewModel insertMessage message $message isProgressProcessLiveData ${isProgressProcessLiveData.value}")
        }
    }

    fun updateMessage(message: Message) {
        viewModelScope.launch {
            chatUseCase.updateMessage(message)
            Log.e("apiTAG", "ChatViewModel updateMessage message $message isProgressProcessLiveData ${isProgressProcessLiveData.value}")
        }
    }
}