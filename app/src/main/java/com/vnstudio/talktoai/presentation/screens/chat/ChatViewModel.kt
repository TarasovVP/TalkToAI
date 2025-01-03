package com.vnstudio.talktoai.presentation.screens.chat

import android.app.Application
import android.util.Log
import com.vnstudio.talktoai.CommonExtensions.isNull
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.network.models.ApiRequest
import com.vnstudio.talktoai.domain.enums.MessageStatus
import com.vnstudio.talktoai.domain.mappers.MessageUIMapper
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.ChatUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import com.vnstudio.talktoai.presentation.ui_models.MessageUIModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch

class ChatViewModel(
    application: Application,
    private val chatUseCase: ChatUseCase,
    private val messageUIMapper: MessageUIMapper
) : BaseViewModel(application) {

    val currentChatLiveData = MutableStateFlow<Chat?>(null)
    val messagesLiveData = MutableStateFlow<List<MessageUIModel>>(listOf())

    private var messagesFlowSubscription: Job? = null

    fun insertChat(chat: Chat) {
        if (chatUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                chatUseCase.insertRemoteChat(chat) { authResult ->
                    when (authResult) {
                        is Result.Success -> {

                        }

                        is Result.Failure -> authResult.errorMessage?.let {
                            _exceptionMessage.value = it
                        }
                    }
                    hideProgress()
                }
            }
        } else {
            launch {
                chatUseCase.insertChat(chat)
            }
        }
    }

    fun getCurrentChat(chatId: Long) {
        showProgress()
        launch {
            chatUseCase.getCurrentChat(chatId).catch {
                hideProgress()
            }.collect { chat ->
                if (chat.isNull()) {
                    currentChatLiveData.value = Chat()
                } else {
                    currentChatLiveData.value = chat
                }
                hideProgress()
            }
        }
    }

    fun getMessagesFromChat(chatId: Long) {
        showProgress()
        messagesFlowSubscription?.cancel()
        messagesFlowSubscription = launch {
            chatUseCase.getMessagesFromChat(chatId).catch {
                hideProgress()
            }.collect { result ->
                messagesLiveData.value = messageUIMapper.mapToUIModelList(result)
                hideProgress()
            }
        }
    }

    fun sendRequest(temporaryMessage: MessageUIModel, apiRequest: ApiRequest) {
        showProgress()
        launch {
            chatUseCase.sendRequest(apiRequest).catch {
                hideProgress()

            }.collect { result ->
                when (result) {
                    is Result.Success -> result.data?.let { apiResponse ->
                        insertMessage(temporaryMessage.apply {
                            author = apiResponse.model.orEmpty()
                            message = apiResponse.choices?.firstOrNull()?.message?.content.orEmpty()
                            updatedAt = apiResponse.created?.toLongOrNull() ?: 0
                            status = MessageStatus.SUCCESS
                        })
                    }

                    is Result.Failure -> {
                        insertMessage(temporaryMessage.apply {
                            status = MessageStatus.ERROR
                            errorMessage = result.errorMessage.orEmpty()
                        })
                    }
                }
                hideProgress()
            }
        }
    }

    fun insertMessage(message: MessageUIModel) {
        if (chatUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                chatUseCase.insertRemoteMessage(messageUIMapper.mapFromUIModel(message)) { authResult ->
                    when (authResult) {
                        is Result.Success -> {

                        }

                        is Result.Failure -> authResult.errorMessage?.let {
                            _exceptionMessage.value = it
                        }
                    }
                    hideProgress()
                }
            }
        } else {
            launch {
                chatUseCase.insertMessage(messageUIMapper.mapFromUIModel(message))
            }
        }
    }

    fun updateMessage(message: MessageUIModel) {
        if (chatUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                chatUseCase.insertRemoteMessage(messageUIMapper.mapFromUIModel(message)) { authResult ->
                    when (authResult) {
                        is Result.Success -> {

                        }

                        is Result.Failure -> authResult.errorMessage?.let {
                            _exceptionMessage.value = it
                        }
                    }
                    hideProgress()
                }
            }
        } else {
            launch {
                chatUseCase.insertMessage(messageUIMapper.mapFromUIModel(message))
            }
        }
    }

    fun deleteMessages(messageIds: List<Long>) {
        showProgress()
        if (chatUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                chatUseCase.deleteRemoteMessages(messageIds) { authResult ->
                    when (authResult) {
                        is Result.Success -> {

                        }

                        is Result.Failure -> authResult.errorMessage?.let {
                            _exceptionMessage.value = it
                        }
                    }
                    hideProgress()
                }
            }
        } else {
            launch {
                chatUseCase.deleteMessages(messageIds)
            }
        }
        hideProgress()
    }

    override fun onCleared() {
        super.onCleared()
        messagesFlowSubscription?.cancel()
    }
}