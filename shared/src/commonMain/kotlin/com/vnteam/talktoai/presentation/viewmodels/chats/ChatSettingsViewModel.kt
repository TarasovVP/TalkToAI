package com.vnteam.talktoai.presentation.viewmodels.chats

import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.AiModels
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.UpdateChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiModelUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiProviderUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatSettingsViewModel(
    private val updateChatUseCase: UpdateChatUseCase,
    private val aiModelUseCase: AiModelUseCase,
    private val aiProviderUseCase: AiProviderUseCase,
) : BaseViewModel() {

    private val _globalAiModel = MutableStateFlow(AiModels.balancedFor(AiProviderType.OPENAI).id)
    val globalAiModel = _globalAiModel.asStateFlow()

    private val _globalProvider = MutableStateFlow(AiProviderType.OPENAI)
    val globalProvider = _globalProvider.asStateFlow()

    private val _chatSaved = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val chatSaved = _chatSaved.asSharedFlow()

    init {
        loadGlobalSettings()
    }

    private fun loadGlobalSettings() {
        launchWithErrorHandling {
            aiModelUseCase.get().collect { result ->
                if (result is Result.Success) {
                    result.data?.takeIf { it.isNotEmpty() }?.let {
                        _globalAiModel.value = it
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

    fun saveChat(chat: Chat) {
        launchWithErrorHandling {
            val result = updateChatUseCase.execute(chat)
            if (result is Result.Success) {
                _chatSaved.emit(Unit)
            }
        }
    }
}
