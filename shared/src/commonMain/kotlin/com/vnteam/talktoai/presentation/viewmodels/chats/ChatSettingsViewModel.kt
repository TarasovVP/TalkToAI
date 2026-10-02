package com.vnteam.talktoai.presentation.viewmodels.chats

import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.enums.ModelTier
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.aimodels.lastAssistantInputTokens
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.parseTier
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.UpdateChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.GetMessagesFromChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiTierUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiProviderUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatSettingsViewModel(
    private val updateChatUseCase: UpdateChatUseCase,
    private val aiTierUseCase: AiTierUseCase,
    private val aiProviderUseCase: AiProviderUseCase,
    private val getMessagesFromChatUseCase: GetMessagesFromChatUseCase,
) : BaseViewModel() {

    private val _globalAiTier = MutableStateFlow(ModelTier.BALANCED)
    val globalAiTier = _globalAiTier.asStateFlow()

    private val _globalProvider = MutableStateFlow(AiProviderType.OPENAI)
    val globalProvider = _globalProvider.asStateFlow()

    private val _contextTokens = MutableStateFlow<Int?>(null)
    val contextTokens = _contextTokens.asStateFlow()

    private val _chatSaved = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val chatSaved = _chatSaved.asSharedFlow()

    init {
        loadGlobalSettings()
    }

    private fun loadGlobalSettings() {
        launchWithErrorHandling {
            aiTierUseCase.get().collect { result ->
                if (result is Result.Success) {
                    result.data?.takeIf { it.isNotEmpty() }?.let {
                        _globalAiTier.value = parseTier(it)
                    }
                }
            }
        }
        launchWithErrorHandling {
            aiProviderUseCase.get().collect { result ->
                if (result is Result.Success && !result.data.isNullOrEmpty()) {
                    _globalProvider.value = runCatching { AiProviderType.valueOf(result.data!!) }
                        .getOrDefault(AiProviderType.OPENAI)
                }
            }
        }
    }

    fun loadContextTokens(chatId: Long) {
        launchWithErrorHandling {
            getMessagesFromChatUseCase.execute(chatId).collect { result ->
                if (result is Result.Success) {
                    _contextTokens.value = lastAssistantInputTokens(result.data.orEmpty())
                }
            }
        }
    }

    fun saveChat(chat: Chat) {
        launchWithErrorHandling {
            val result = updateChatUseCase.execute(chat)
            if (result is Result.Success) {
                _chatSaved.emit(Unit)
            }
        }
    }
}
