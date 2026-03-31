package com.vnteam.talktoai.presentation.viewmodels.chats

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.data.network.ai.request.ApiRequest
import com.vnteam.talktoai.data.network.ai.request.MessageApi
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
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiModelUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ApiKeyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.TemperatureUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.AnimationUtils
import com.vnteam.talktoai.utils.ShareUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull

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
    private val sendRequestUseCase: SendRequestUseCase,
    private val aiModelUseCase: AiModelUseCase,
    private val apiKeyUseCase: ApiKeyUseCase,
    private val temperatureUseCase: TemperatureUseCase,
) : BaseViewModel() {

    private val _currentChatLiveData = MutableStateFlow<ChatUI?>(null)
    val currentChatLiveData = _currentChatLiveData.asStateFlow()
    private val _messagesLiveData = MutableStateFlow<List<MessageUI>>(listOf())
    val messagesLiveData = _messagesLiveData.asStateFlow()
    private val _animationResource = MutableStateFlow("")
    val animationResource = _animationResource.asStateFlow()
    private val _aiModel = MutableStateFlow(SettingsConstants.AI_MODEL_DEFAULT)
    private val _apiKey = MutableStateFlow<String?>(null)
    private val _temperature = MutableStateFlow(SettingsConstants.AI_TEMPERATURE_DEFAULT)

    init {
        launchWithErrorHandling {
            aiModelUseCase.get().firstOrNull()?.let { result ->
                if (result is com.vnteam.talktoai.data.network.Result.Success && !result.data.isNullOrEmpty()) {
                    _aiModel.value = result.data!!
                }
            }
        }
        launchWithErrorHandling {
            apiKeyUseCase.get().firstOrNull()?.let { result ->
                if (result is com.vnteam.talktoai.data.network.Result.Success && !result.data.isNullOrEmpty()) {
                    _apiKey.value = result.data
                }
            }
        }
        launchWithErrorHandling {
            temperatureUseCase.get().firstOrNull()?.let { result ->
                if (result is com.vnteam.talktoai.data.network.Result.Success) {
                    _temperature.value = result.data ?: SettingsConstants.AI_TEMPERATURE_DEFAULT
                }
            }
        }
    }

    fun insertChat(chat: Chat) {
        launchWithResult {
            insertChatUseCase.execute(chat)
        }
    }

    fun getCurrentChat(chatId: Long) {
        launchWithResultHandling {
            getChatWithIdUseCase.execute(chatId).onSuccess { chat ->
                _currentChatLiveData.value = chat?.let { chatUIMapper.mapToImplModel(it) }
            }
        }
    }

    fun getMessagesFromChat(chatId: Long) {
        launchWithResultHandling {
            getMessagesFromChatUseCase.execute(chatId).onSuccess { result ->
                val checkedIds = _messagesLiveData.value
                    .filter { it.isCheckedToDelete.value }
                    .map { it.id }
                    .toSet()
                val newMessages = messageUIMapper.mapToImplModelList(result.orEmpty())
                if (checkedIds.isNotEmpty()) {
                    newMessages.forEach { if (it.id in checkedIds) it.isCheckedToDelete.value = true }
                }
                _messagesLiveData.value = newMessages
            }
        }
    }

    fun sendRequest(temporaryMessage: MessageUI, messageText: String) {
        val apiRequest = ApiRequest(
            model = _aiModel.value,
            temperature = _temperature.value,
            messages = listOf(MessageApi(role = Constants.MESSAGE_ROLE_USER, content = messageText))
        )
        println("messageTAG ChatViewModel.sendRequest: apiRequest = $apiRequest")
        launchWithResultHandling {
            sendRequestUseCase.execute(apiRequest, _apiKey.value).onSuccess { apiResponse ->
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

    fun getAnimationResource() {
        launchWithErrorHandling {
            val resource = Res.readBytes("files/message_typing.json").decodeToString()
            _animationResource.value = resource
        }
    }
}