package com.vnteam.talktoai.presentation.viewmodels.chats

import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.UpdateChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiModelUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.TemperatureUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatSettingsViewModel(
    private val updateChatUseCase: UpdateChatUseCase,
    private val aiModelUseCase: AiModelUseCase,
    private val temperatureUseCase: TemperatureUseCase,
) : BaseViewModel() {

    private val _globalAiModel = MutableStateFlow(SettingsConstants.AI_MODEL_DEFAULT)
    val globalAiModel = _globalAiModel.asStateFlow()

    private val _globalTemperature = MutableStateFlow(SettingsConstants.AI_TEMPERATURE_DEFAULT)
    val globalTemperature = _globalTemperature.asStateFlow()

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
            temperatureUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val temp = result.data ?: SettingsConstants.AI_TEMPERATURE_DEFAULT
                    _globalTemperature.value = temp
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
