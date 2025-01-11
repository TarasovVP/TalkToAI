package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.domain.usecase.SettingsChatUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsChatViewModel(
    private val settingsChatUseCase: SettingsChatUseCase,
) : BaseViewModel() {

    val chatSettingsLiveData = MutableStateFlow(false)

    fun testProgressVisibilityChange() {
        launchWithErrorHandling {
            showProgress()
            delay(3000)
            hideProgress()
        }
    }

    fun testExceptionMessage() {
        showMessage("Test exception message")
    }
}