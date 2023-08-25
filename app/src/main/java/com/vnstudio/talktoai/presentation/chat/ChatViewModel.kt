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

    val chatsLiveData = MutableLiveData<List<Chat>>()
    val currentChatLiveData = MutableLiveData<Chat>()
    val messagesLiveData = MutableLiveData<List<Message>>()

    fun getChats() {
        showProgress()
        launch {
            chatUseCase.getChats().catch {
                hideProgress()
                Log.e("apiTAG", "MainViewModel getChats catch localizedMessage ${it.localizedMessage}")
            }.collect { chats ->
                chatsLiveData.postValue(chats)
                hideProgress()
                Log.e("apiTAG", "MainViewModel getChats chats $chats")
            }
        }
    }

    fun updateChat(chat: Chat) {
        showProgress()
        launch {
            chatUseCase.updateChat(chat)
        }
    }

    fun deleteChat(chat: Chat) {
        showProgress()
        launch {
            chatUseCase.deleteChat(chat)
            chatUseCase.deleteMessagesFromChat(chat.chatId)
        }
    }

    fun updateChats(chats: List<Chat>) {
        showProgress()
        launch {
            chatUseCase.updateChats(chats)
        }
    }

    fun insertChat(chat: Chat) {
        showProgress()
        launch {
            chatUseCase.insertChat(chat)
        }
    }

    fun getCurrentChat() {
        launch {
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
        Log.e("messagesTAG", "ChatViewModel getMessagesFromChat before messagesLiveData ${messagesLiveData.value?.map { it.message }}")
        launch {
            chatUseCase.getMessagesFromChat(chatId).catch {
                hideProgress()
                Log.e("apiTAG", "ChatViewModel getMessagesFromChat catch localizedMessage ${it.localizedMessage} isProgressProcessLiveData ${isProgressProcessLiveData.value}")
            }.collect { result ->
                Log.e("apiTAG", "ChatViewModel getMessagesFromChat collect result.size ${result.size} chatId $chatId isProgressProcessLiveData ${isProgressProcessLiveData.value}")
                Log.e("messagesTAG", "ChatViewModel getMessagesFromChat after messagesLiveData ${messagesLiveData.value?.map { it.message }}")
                messagesLiveData.postValue(result)

            }
        }
    }

    fun sendRequest(apiRequest: ApiRequest) {
        Log.e("messagesTAG", "ChatViewModel sendRequest messagesLiveData ${messagesLiveData.value?.map { it.message }}")
        showProgress()
        launch {
            chatUseCase.sendRequest(apiRequest).catch {
                hideProgress()
                Log.e("apiTAG", "ChatViewModel sendRequest catch localizedMessage ${it.localizedMessage} isProgressProcessLiveData ${isProgressProcessLiveData.value}")
            }.collect { result ->

                when(result) {
                    is Result.Success -> result.data?.let { apiResponse ->
                        val updatedMessage = Message(messageId = messagesLiveData.value?.lastOrNull()?.messageId ?: 0,
                            chatId = messagesLiveData.value?.lastOrNull()?.chatId ?: 0,
                            author = apiResponse.model.orEmpty(),
                            message = apiResponse.choices?.firstOrNull()?.message?.content.orEmpty(),
                            createdAt = apiResponse.created?.toLongOrNull() ?: 0)
                        updateMessage(updatedMessage)
                    }
                    is Result.Failure -> Log.e("apiTAG", "ChatViewModel sendRequest Result.Failure localizedMessage ${result.errorMessage}  isProgressProcessLiveData ${isProgressProcessLiveData.value}")
                }
                hideProgress()
                Log.e("apiTAG", "ChatViewModel sendRequest result $result  isProgressProcessLiveData ${isProgressProcessLiveData.value}")
            }
        }
    }

    fun insertMessage(message: Message) {
        Log.e("messagesTAG", "ChatViewModel insertMessage messagesLiveData ${messagesLiveData.value?.map { it.message }}")
        launch {
            chatUseCase.insertMessage(message)
            Log.e("apiTAG", "ChatViewModel insertMessage message $message isProgressProcessLiveData ${isProgressProcessLiveData.value}")
        }
    }

    private fun updateMessage(message: Message) {
        Log.e("messagesTAG", "ChatViewModel updateMessage messagesLiveData ${messagesLiveData.value?.map { it.message }}")
            launch {
            chatUseCase.updateMessage(message)
            Log.e("apiTAG", "ChatViewModel updateMessage message $message isProgressProcessLiveData ${isProgressProcessLiveData.value}")
        }
    }
}