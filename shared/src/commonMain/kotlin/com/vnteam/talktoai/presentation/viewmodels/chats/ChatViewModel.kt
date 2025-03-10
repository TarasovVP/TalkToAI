package com.vnteam.talktoai.presentation.viewmodels.chats

import com.vnteam.talktoai.Res
import com.vnteam.talktoai.data.network.ai.request.ApiRequest
import com.vnteam.talktoai.data.network.onError
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.mappers.ChatUIMapper
import com.vnteam.talktoai.domain.mappers.MessageUIMapper
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.uimodels.ChatUI
import com.vnteam.talktoai.presentation.uimodels.MessageUI
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.ai.SendRequestUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.GetChatWithIdUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.InsertChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.DeleteMessagesUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.GetMessagesFromChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.InsertMessageUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.AnimationUtils
import com.vnteam.talktoai.utils.ShareUtils
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.ExperimentalResourceApi

class ChatViewModel(
    private val messageUIMapper: MessageUIMapper,
    private val chatUIMapper: ChatUIMapper,
    private val shareUtils: ShareUtils,
    val animationUtils: AnimationUtils,
    private val insertChatUseCase: InsertChatUseCase,
    private val getChatWithIdUseCase: GetChatWithIdUseCase,
    private val deleteMessagesUseCase: DeleteMessagesUseCase,
    private val getMessagesFromChatUseCase: GetMessagesFromChatUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    private val sendRequestUseCase: SendRequestUseCase
) : BaseViewModel() {

    val currentChatLiveData = MutableStateFlow<ChatUI?>(null)
    val messagesLiveData = MutableStateFlow<List<MessageUI>>(listOf())
    val animationResource = MutableStateFlow("")

    fun insertChat(chat: Chat) {
        launchWithResult {
            insertChatUseCase.execute(chat)
        }
    }

    fun getCurrentChat(chatId: Long) {
        launchWithResultHandling {
            getChatWithIdUseCase.execute(chatId).onSuccess { chat ->
                currentChatLiveData.value = chat?.let { chatUIMapper.mapToImplModel(it) }
            }
        }
    }

    fun getMessagesFromChat(chatId: Long) {
        launchWithResultHandling {
            getMessagesFromChatUseCase.execute(chatId).onSuccess { result ->
                messagesLiveData.value = messageUIMapper.mapToImplModelList(result.orEmpty())
            }
        }
    }

    fun sendRequest(temporaryMessage: MessageUI, apiRequest: ApiRequest) {
        println("messageTAG ChatViewModel.sendRequest: apiRequest = $apiRequest")
        launchWithResultHandling {
            sendRequestUseCase.execute(apiRequest).onSuccess { apiResponse ->
                println("messageTAG ChatViewModel.sendRequest: apiResponse = $apiResponse")
                insertMessage(
                    temporaryMessage.copy(
                        author = apiResponse?.model.orEmpty(),
                        message = apiResponse?.choices?.firstOrNull()?.message?.content.orEmpty(),
                        //updatedAt = apiResponse?.created?.toLongOrNull() ?: 0,
                        status = MessageStatus.SUCCESS
                    )
                )
            }.onError { error ->
                insertMessage(
                    temporaryMessage.copy(
                        status = MessageStatus.ERROR,
                        errorMessage = error.orEmpty()
                    )
                )
            }
        }
    }

    fun insertMessage(message: MessageUI) {
        launchWithResult {
            println("messageTAG ChatViewModel.insertMessage: message = $message")
            insertMessageUseCase.execute(messageUIMapper.mapFromImplModel(message))
        }
    }

    fun deleteMessages(messageIds: List<Long>) {
        launchWithResult {
            deleteMessagesUseCase.execute(messageIds)
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
}