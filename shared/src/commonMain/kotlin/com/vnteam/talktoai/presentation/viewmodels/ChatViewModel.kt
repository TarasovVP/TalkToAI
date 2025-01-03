package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.CommonExtensions.isNull
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.data.network.request.ApiRequest
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.mappers.ChatUIMapper
import com.vnteam.talktoai.domain.mappers.MessageUIMapper
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.usecase.ChatUseCase
import com.vnteam.talktoai.presentation.uimodels.ChatUI
import com.vnteam.talktoai.presentation.uimodels.MessageUI
import com.vnteam.talktoai.utils.AnimationUtils
import com.vnteam.talktoai.utils.ShareUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import org.jetbrains.compose.resources.ExperimentalResourceApi

class ChatViewModel(
    private val chatUseCase: ChatUseCase,
    private val messageUIMapper: MessageUIMapper,
    private val chatUIMapper: ChatUIMapper,
    private val shareUtils: ShareUtils,
    val animationUtils: AnimationUtils
) : BaseViewModel() {

    val currentChatLiveData = MutableStateFlow<ChatUI?>(null)
    val messagesLiveData = MutableStateFlow<List<MessageUI>>(listOf())
    val animationResource = MutableStateFlow("")

    private var messagesFlowSubscription: Job? = null

    fun insertChat(chat: Chat) {
        if (chatUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                chatUseCase.insertRemoteChat(chat) { authResult ->
                    when (authResult) {
                        is NetworkResult.Success -> {

                        }

                        is NetworkResult.Failure -> authResult.errorMessage?.let {
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
                    currentChatLiveData.value = ChatUI()
                } else {
                    currentChatLiveData.value = chat?.let { chatUIMapper.mapToImplModel(it) }
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
                messagesLiveData.value = messageUIMapper.mapToImplModelList(result)
                hideProgress()
            }
        }
    }

    fun sendRequest(temporaryMessage: MessageUI, apiRequest: ApiRequest) {
        showProgress()
        launch {
            chatUseCase.sendRequest(apiRequest).catch {
                hideProgress()

            }.collect { result ->
                when (result) {
                    is NetworkResult.Success -> result.data?.let { apiResponse ->
                        insertMessage(temporaryMessage.apply {
                            author = apiResponse.model.orEmpty()
                            message = apiResponse.choices?.firstOrNull()?.message?.content.orEmpty()
                            updatedAt = apiResponse.created?.toLongOrNull() ?: 0
                            status = MessageStatus.SUCCESS
                        })
                    }

                    is NetworkResult.Failure -> {
                        insertMessage(temporaryMessage.apply {
                            status = MessageStatus.ERROR
                            errorMessage = result.errorMessage.orEmpty()
                            println("Error when api request: ${result.errorMessage}")
                        })
                    }
                }
                hideProgress()
            }
        }
    }

    fun insertMessage(message: MessageUI) {
        if (chatUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                chatUseCase.insertRemoteMessage(messageUIMapper.mapFromImplModel(message)) { authResult ->
                    when (authResult) {
                        is NetworkResult.Success -> {

                        }

                        is NetworkResult.Failure -> authResult.errorMessage?.let {
                            _exceptionMessage.value = it
                        }
                    }
                    hideProgress()
                }
            }
        } else {
            launch {
                chatUseCase.insertMessage(messageUIMapper.mapFromImplModel(message))
            }
        }
    }

    fun updateMessage(message: MessageUI) {
        if (chatUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                chatUseCase.insertRemoteMessage(messageUIMapper.mapFromImplModel(message)) { authResult ->
                    when (authResult) {
                        is NetworkResult.Success -> {

                        }

                        is NetworkResult.Failure -> authResult.errorMessage?.let {
                            _exceptionMessage.value = it
                        }
                    }
                    hideProgress()
                }
            }
        } else {
            launch {
                chatUseCase.insertMessage(messageUIMapper.mapFromImplModel(message))
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
                        is NetworkResult.Success -> {

                        }

                        is NetworkResult.Failure -> authResult.errorMessage?.let {
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

    fun shareLink(text: String) {
        shareUtils.shareLink(text)
    }

    @OptIn(ExperimentalResourceApi::class)
    fun getAnimationResource() {
        launch {
            val resource = Res.readBytes("files/message_typing.json").decodeToString()
            animationResource.value = resource
        }
    }

    override fun onCleared() {
        super.onCleared()
        messagesFlowSubscription?.cancel()
    }
}