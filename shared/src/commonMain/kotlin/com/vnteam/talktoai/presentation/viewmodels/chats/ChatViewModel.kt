package com.vnteam.talktoai.presentation.viewmodels.chats

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.dateToMilliseconds
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.resolveEffectiveProvider
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.mappers.ChatUIMapper
import com.vnteam.talktoai.domain.mappers.MessageUIMapper
import com.vnteam.talktoai.domain.models.AiModels
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.uimodels.ChatUI
import com.vnteam.talktoai.presentation.uimodels.MessageUI
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.ai.SendRequestUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.GetChatWithIdUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.InsertChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.UpdateChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.DeleteMessagesUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.GetMessagesFromChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.InsertMessageUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiModelUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiProviderUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.GlobalContextUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.AnimationUtils
import com.vnteam.talktoai.utils.ShareUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Clock
import com.vnteam.talktoai.data.network.ai.request.Message as AiMessage

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
    private val updateChatUseCase: UpdateChatUseCase,
    private val aiModelUseCase: AiModelUseCase,
    private val aiProviderUseCase: AiProviderUseCase,
    private val globalContextUseCase: GlobalContextUseCase,
) : BaseViewModel() {

    private val _currentChatLiveData = MutableStateFlow<ChatUI?>(null)
    val currentChatLiveData = _currentChatLiveData.asStateFlow()
    private val _welcomeChat = MutableStateFlow<Chat?>(null)
    val welcomeChat = _welcomeChat.asStateFlow()
    private val _messagesLiveData = MutableStateFlow<List<MessageUI>?>(null)
    val messagesLiveData = _messagesLiveData.asStateFlow()
    private val _animationResource = MutableStateFlow("")
    val animationResource = _animationResource.asStateFlow()
    private val _aiModel = MutableStateFlow(SettingsConstants.OPENAI_AI_MODEL_DEFAULT)
    private val _globalProvider = MutableStateFlow(AiProviderType.OPENAI)
    val globalProvider = _globalProvider.asStateFlow()
    private val _globalContext = MutableStateFlow<String?>(null)

    private val _modelFallback = MutableSharedFlow<Pair<String, String>>(extraBufferCapacity = 1)
    val modelFallback = _modelFallback.asSharedFlow()

    init {
        launchWithErrorHandling {
            aiModelUseCase.get().firstOrNull()?.let { result ->
                if (result is Result.Success && !result.data.isNullOrEmpty()) {
                    _aiModel.value = result.data!!
                }
            }
        }
        launchWithErrorHandling {
            aiProviderUseCase.get().collect { result ->
                if (result is Result.Success && !result.data.isNullOrEmpty()) {
                    _globalProvider.value = runCatching { AiProviderType.valueOf(result.data!!) }.getOrDefault(AiProviderType.OPENAI)
                }
            }
        }
        launchWithErrorHandling {
            globalContextUseCase.get().collect { result ->
                if (result is Result.Success) {
                    _globalContext.value = result.data
                }
            }
        }
    }

    fun insertChat(chat: Chat) {
        launchWithResult {
            insertChatUseCase.execute(chat).onSuccess { insertedChat ->
                if (insertedChat == null) return@onSuccess
                _currentChatLiveData.value = chatUIMapper.mapToImplModel(insertedChat)
            }
        }
    }

    fun createWelcomeChat(chatName: String, welcomeMessage: String) {
        launchWithErrorHandling {
            val firstResult = getChatWithIdUseCase.execute(Constants.DEFAULT_CHAT_ID).firstOrNull()
            val existingChat = (firstResult as? Result.Success)?.data
            if (existingChat?.id != null) {
                _currentChatLiveData.value = chatUIMapper.mapToImplModel(existingChat)
                _welcomeChat.value = existingChat
                return@launchWithErrorHandling
            }
            val chatId = Clock.System.now().dateToMilliseconds()
            val chat = Chat(id = chatId, name = chatName, updated = chatId, listOrder = 1)
            val insertedChat = when (val result = insertChatUseCase.execute(chat)) {
                is Result.Success -> result.data
                else -> null
            } ?: return@launchWithErrorHandling
            insertMessageUseCase.execute(
                messageUIMapper.mapFromImplModel(
                    MessageUI(
                        id = chatId + 1,
                        chatId = chatId,
                        author = Constants.MESSAGE_ROLE_CHAT_GPT,
                        message = welcomeMessage,
                        updatedAt = chatId,
                        status = MessageStatus.SUCCESS
                    )
                )
            )
            _messagesLiveData.value = null
            _currentChatLiveData.value = chatUIMapper.mapToImplModel(insertedChat)
            _welcomeChat.value = insertedChat
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
        _messagesLiveData.value = null
        launchWithResultHandling {
            getMessagesFromChatUseCase.execute(chatId).onSuccess { result ->
                val checkedIds = _messagesLiveData.value
                    .orEmpty()
                    .filter { it.isCheckedToDelete.value }
                    .map { it.id }
                    .toSet()
                val newMessages = messageUIMapper.mapToImplModelList(result.orEmpty())
                if (checkedIds.isNotEmpty()) {
                    newMessages.forEach {
                        if (it.id in checkedIds) it.isCheckedToDelete.value = true
                    }
                }
                _messagesLiveData.value = newMessages
            }
        }
    }

    fun sendMessage(chatId: Long, messageText: String) {
        val now = Clock.System.now()
        val userMsgId = now.toEpochMilliseconds()
        val userMsg = MessageUI(
            id = userMsgId,
            chatId = chatId,
            author = Constants.MESSAGE_ROLE_ME,
            message = messageText,
            updatedAt = now.dateToMilliseconds(),
            status = MessageStatus.SUCCESS
        )
        val tempMsg = MessageUI(
            id = userMsgId + 1,
            chatId = chatId,
            author = Constants.MESSAGE_ROLE_CHAT_GPT,
            message = "",
            updatedAt = now.dateToMilliseconds() + 1,
            status = MessageStatus.REQUESTING
        )
        val currentChat = _currentChatLiveData.value
        val history = _messagesLiveData.value.orEmpty()
        insertMessage(userMsg)
        insertMessage(tempMsg)
        val combinedContext = listOfNotNull(
            _globalContext.value?.takeIf { it.isNotBlank() },
            currentChat?.context?.takeIf { it.isNotBlank() }
        ).joinToString("\n").takeIf { it.isNotBlank() }
        sendRequest(
            temporaryMessage = tempMsg,
            messageText = messageText,
            systemContext = combinedContext,
            chatAiModel = currentChat?.aiModel,
            chatTemperature = currentChat?.temperature,
            history = history,
        )
    }

    private fun sendRequest(
        temporaryMessage: MessageUI,
        messageText: String,
        systemContext: String?,
        chatAiModel: String?,
        chatTemperature: Float?,
        history: List<MessageUI>,
    ) {
        var remainingTokens = MAX_HISTORY_TOKENS -
                estimateTokens(systemContext.orEmpty()) -
                estimateTokens(messageText)
        val trimmedHistory = history
            .filter { it.status == MessageStatus.SUCCESS && it.message.isNotEmpty() }
            .reversed()
            .filter { msg ->
                val tokens = estimateTokens(msg.message)
                (tokens <= remainingTokens).also { fits -> if (fits) remainingTokens -= tokens }
            }
            .reversed()
        val messages = buildList {
            if (!systemContext.isNullOrBlank()) {
                add(AiMessage(role = Constants.MESSAGE_ROLE_SYSTEM, content = systemContext))
            }
            trimmedHistory.forEach { msg ->
                add(
                    AiMessage(
                        role = if (msg.author == Constants.MESSAGE_ROLE_ME) Constants.MESSAGE_ROLE_USER else Constants.MESSAGE_ROLE_ASSISTANT,
                        content = msg.message
                    )
                )
            }
            add(AiMessage(role = Constants.MESSAGE_ROLE_USER, content = messageText))
        }
        val model = chatAiModel ?: _aiModel.value
        val providerType = resolveEffectiveProvider(_currentChatLiveData.value?.aiProvider, _globalProvider.value)
        val supportsTemperature = AiModels.forProvider(providerType).find { it.id == model }?.supportsTemperature ?: false
        val temperature = if (supportsTemperature) chatTemperature else null
        launchWithErrorHandling {
            val result = sendRequestUseCase.execute(model, messages, null, providerType, temperature).firstOrNull()
            when (result) {
                is Result.Success -> {
                    val aiResponse = result.data
                    val fallbackFrom = aiResponse?.fallbackFrom
                    if (fallbackFrom != null) {
                        val currentChat = _currentChatLiveData.value
                        val chatDomain = currentChat?.let { chatUIMapper.mapFromImplModel(it) }
                        if (chatDomain?.aiModel == fallbackFrom) {
                            val updated = chatDomain.copy(aiModel = aiResponse.model)
                            updateChatUseCase.execute(updated)
                            _currentChatLiveData.value = chatUIMapper.mapToImplModel(updated)
                        }
                        _modelFallback.emit(Pair(fallbackFrom, aiResponse.model))
                    }
                    insertMessage(
                        temporaryMessage.copy(
                            author = aiResponse?.model.orEmpty(),
                            message = aiResponse?.content.orEmpty(),
                            status = MessageStatus.SUCCESS
                        )
                    )
                }
                is Result.Failure -> {
                    insertMessage(
                        temporaryMessage.copy(
                            status = MessageStatus.ERROR,
                            errorMessage = result.errorMessage.orEmpty()
                        )
                    )
                }
                else -> Unit
            }
        }
    }

    fun insertMessage(message: MessageUI) {
        launchWithErrorHandling {
            insertMessageUseCase.execute(messageUIMapper.mapFromImplModel(message))
            val current = _messagesLiveData.value.orEmpty().toMutableList()
            val idx = current.indexOfFirst { it.id == message.id }
            if (idx >= 0) current[idx] = message else current.add(message)
            _messagesLiveData.value = current
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

    companion object {
        private const val MAX_HISTORY_TOKENS = 4000
        private fun estimateTokens(text: String): Int = (text.length / 4).coerceAtLeast(1)
    }
}
