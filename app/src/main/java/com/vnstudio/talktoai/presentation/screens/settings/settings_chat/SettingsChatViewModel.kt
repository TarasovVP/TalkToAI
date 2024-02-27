package com.vnstudio.talktoai.presentation.screens.settings.settings_chat

import android.app.Application
import com.vnstudio.talktoai.domain.usecases.SettingsChatUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsChatViewModel(
    application: Application,
    private val settingsChatUseCase: SettingsChatUseCase,
) : BaseViewModel(application) {

    val chatSettingsLiveData = MutableStateFlow(false)

    fun getChatSettings() {
        launch {
            settingsChatUseCase.getChatSettings().collect { blockerTurnOn ->
                chatSettingsLiveData.value = blockerTurnOn ?: true
            }
        }
    }
}