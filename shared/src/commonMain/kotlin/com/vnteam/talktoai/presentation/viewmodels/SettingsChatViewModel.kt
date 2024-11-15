package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.domain.usecase.SettingsChatUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsChatViewModel(
    private val settingsChatUseCase: SettingsChatUseCase,
) : BaseViewModel() {

    val chatSettingsLiveData = MutableStateFlow(false)

    fun getChatSettings() {
        launch {
            settingsChatUseCase.getChatSettings().collect { blockerTurnOn ->
                chatSettingsLiveData.value = blockerTurnOn ?: true
            }
        }
    }
}