package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.usecase.SettingsThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsThemeViewModel(
    private val settingsThemeUseCase: SettingsThemeUseCase,
) : BaseViewModel() {

    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme.asStateFlow()

    fun getIsDarkTheme() {
        launchWithResultHandling {
            settingsThemeUseCase.getAppTheme().onSuccess {
                _isDarkTheme.value = it
            }
        }
    }

    fun setIsDarkTheme(isDarkTheme: Boolean) {
        launchWithErrorHandling {
            settingsThemeUseCase.setAppTheme(isDarkTheme)
            _isDarkTheme.value = isDarkTheme
        }
    }
}