package com.vnstudio.talktoai.presentation.screens.settings.settings_chat

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.domain.usecases.SettingsChatUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel



class SettingsChatViewModel(
    application: Application,
    private val settingsChatUseCase: SettingsChatUseCase,
) : BaseViewModel(application) {

    val chatSettingsLiveData = MutableLiveData<Boolean>()

    fun getChatSettings() {
        launch {
            settingsChatUseCase.getChatSettings().collect { blockerTurnOn ->
                chatSettingsLiveData.postValue(blockerTurnOn ?: true)
            }
        }
    }
}