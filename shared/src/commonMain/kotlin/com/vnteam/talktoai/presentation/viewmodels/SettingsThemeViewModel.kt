package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.domain.usecase.SettingsThemeUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsThemeViewModel(
    private val settingsThemeUseCase: SettingsThemeUseCase,
) : BaseViewModel() {

    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme.asStateFlow()

    fun getIsDarkTheme() {
        launch {
            _progressVisibilityState.value = true
            delay(2000)
            settingsThemeUseCase.getAppTheme().collect { appTheme ->
                _isDarkTheme.value = appTheme
            }
        }
    }

    fun setIsDarkTheme(isDarkTheme: Boolean) {
        launch {
            settingsThemeUseCase.setAppTheme(isDarkTheme)
            _isDarkTheme.value = isDarkTheme
        }
    }
}