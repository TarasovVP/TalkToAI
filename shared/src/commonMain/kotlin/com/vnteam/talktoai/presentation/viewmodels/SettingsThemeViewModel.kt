package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.preferences.ThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsThemeViewModel(
    private val themeUseCase: ThemeUseCase,
) : BaseViewModel() {

    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun getIsDarkTheme() {
        launchWithResultHandling {
            themeUseCase.get().onSuccess {
                _isDarkTheme.value = it
            }
        }
    }

    fun setIsDarkTheme(isDarkTheme: Boolean) {
        launchWithErrorHandling {
            themeUseCase.set(isDarkTheme)
            _isDarkTheme.value = isDarkTheme
        }
    }
}