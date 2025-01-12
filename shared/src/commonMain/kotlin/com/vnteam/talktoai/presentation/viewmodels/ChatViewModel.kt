package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.CommonExtensions.isNull
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.data.network.onError
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.data.network.request.ApiRequest
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.mappers.ChatUIMapper
import com.vnteam.talktoai.domain.mappers.MessageUIMapper
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.usecase.ChatUseCase
import com.vnteam.talktoai.presentation.uimodels.ChatUI
import com.vnteam.talktoai.presentation.uimodels.MessageUI
import com.vnteam.talktoai.utils.AnimationUtils
import com.vnteam.talktoai.utils.NetworkState
import com.vnteam.talktoai.utils.ShareUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.ExperimentalResourceApi

class ChatViewModel(
    private val chatUseCase: ChatUseCase,
    private val messageUIMapper: MessageUIMapper,
    private val chatUIMapper: ChatUIMapper,
    private val shareUtils: ShareUtils,
    private val networkState: NetworkState,
    val animationUtils: AnimationUtils
) : BaseViewModel() {

    val currentChatLiveData = MutableStateFlow<ChatUI?>(null)
    val messagesLiveData = MutableStateFlow<List<MessageUI>>(listOf())
    val animationResource = MutableStateFlow("")

    private var messagesFlowSubscription: Job? = null

    fun insertChat(chat: Chat) {
        if (chatUseCase.isAuthorisedUser()) {
            launchWithNetworkCheck(networkState) {
                chatUseCase.insertRemoteChat(chat).onSuccess {

                }
            }
        } else {
            launchWithErrorHandling {
                chatUseCase.insertChat(chat)
            }
        }
    }

    fun getCurrentChat(chatId: Long) {
        launchWithResultHandling {
            chatUseCase.getCurrentChat(chatId).onSuccess { chat ->
                if (chat.isNull()) {
                    currentChatLiveData.value = ChatUI()
                } else {
                    currentChatLiveData.value = chat?.let { chatUIMapper.mapToImplModel(it) }
                }
            }
        }
    }

    fun getMessagesFromChat(chatId: Long) {
        showProgress()
        messagesFlowSubscription?.cancel()
        messagesFlowSubscription = launchWithResultHandling {
            chatUseCase.getMessagesFromChat(chatId).onSuccess { result ->
                messagesLiveData.value = messageUIMapper.mapToImplModelList(result.orEmpty())
            }
        }
    }

    fun sendRequest(temporaryMessage: MessageUI, apiRequest: ApiRequest) {
        launchWithNetworkCheck(networkState) {
            chatUseCase.sendRequest(apiRequest).onSuccess { apiResponse ->
                insertMessage(temporaryMessage.apply {
                    author = apiResponse?.model.orEmpty()
                    message = apiResponse?.choices?.firstOrNull()?.message?.content.orEmpty()
                    updatedAt = apiResponse?.created?.toLongOrNull() ?: 0
                    status = MessageStatus.SUCCESS
                })
            }.onError { error ->
                insertMessage(temporaryMessage.apply {
                    status = MessageStatus.ERROR
                    errorMessage = error.orEmpty()
                    println("Error when api request: $error")
                })
            }
        }
    }

    fun insertMessage(message: MessageUI) {
        if (chatUseCase.isAuthorisedUser()) {
            launchWithNetworkCheck(networkState) {
                chatUseCase.insertRemoteMessage(messageUIMapper.mapFromImplModel(message)).onSuccess {

                }
            }
        } else {
            launchWithErrorHandling {
                chatUseCase.insertMessage(messageUIMapper.mapFromImplModel(message))
                // TODO add temporary
                getMessagesFromChat(message.chatId)
            }
        }
    }

    fun updateMessage(message: MessageUI) {
        if (chatUseCase.isAuthorisedUser()) {
            launchWithNetworkCheck(networkState) {
                chatUseCase.insertRemoteMessage(messageUIMapper.mapFromImplModel(message)).onSuccess {

                }
            }
        } else {
            launchWithErrorHandling {
                chatUseCase.insertMessage(messageUIMapper.mapFromImplModel(message))
            }
        }
    }

    fun deleteMessages(messageIds: List<Long>) {
        if (chatUseCase.isAuthorisedUser()) {
            launchWithNetworkCheck(networkState) {
                chatUseCase.deleteRemoteMessages(messageIds).onSuccess {

                }
            }
        } else {
            launchWithErrorHandling {
                chatUseCase.deleteMessages(messageIds)
            }
        }
    }

    fun shareLink(text: String) {
        shareUtils.shareLink(text)
    }

    @OptIn(ExperimentalResourceApi::class)
    fun getAnimationResource() {
        launchWithErrorHandling {
            val resource = Res.readBytes("files/message_typing.json").decodeToString()
            animationResource.value = resource
        }
    }

    override fun onCleared() {
        super.onCleared()
        messagesFlowSubscription?.cancel()
    }
}