package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.domain.usecase.SettingsThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsThemeViewModel(
    private val settingsThemeUseCase: SettingsThemeUseCase,
) : BaseViewModel() {

    val appThemeLiveData = MutableStateFlow(0)

    fun getAppTheme() {
        launch {
            settingsThemeUseCase.getAppTheme().collect { appTheme ->
               // appThemeLiveData.value = appTheme ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
    }

    fun setAppTheme(appTheme: Int) {
        launch {
            //settingsThemeUseCase.setAppTheme(appTheme)
            appThemeLiveData.value = appTheme
        }
    }
}