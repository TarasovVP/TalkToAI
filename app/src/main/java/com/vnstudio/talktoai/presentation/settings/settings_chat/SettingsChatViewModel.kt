package com.vnstudio.talktoai.presentation.settings.settings_chat

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.domain.usecases.SettingsBlockerUseCase
import com.vnstudio.talktoai.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsChatViewModel @Inject constructor(
    private val application: Application,
    private val settingsBlockerUseCase: SettingsBlockerUseCase
) : BaseViewModel(application) {

    val blockerTurnOnLiveData = MutableLiveData<Boolean>()

    fun getBlockerTurnOn() {
        launch {
            settingsBlockerUseCase.getBlockerTurnOn().collect { blockerTurnOn ->
                blockerTurnOnLiveData.postValue(blockerTurnOn ?: true)
            }
        }
    }
}