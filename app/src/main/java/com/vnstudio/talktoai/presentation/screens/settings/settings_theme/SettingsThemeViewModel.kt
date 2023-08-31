package com.vnstudio.talktoai.presentation.screens.settings.settings_theme

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.domain.usecases.SettingsThemeUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsThemeViewModel @Inject constructor(
    application: Application,
    private val settingsThemeUseCase: SettingsThemeUseCase,
) : BaseViewModel(application) {

    val appThemeLiveData = MutableLiveData<Int>()

    fun getAppTheme() {
        launch {
            settingsThemeUseCase.getAppTheme().collect { appTheme ->
                appThemeLiveData.postValue(appTheme ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    fun setAppTheme(appTheme: Int) {
        launch {
            settingsThemeUseCase.setAppTheme(appTheme)
            appThemeLiveData.postValue(appTheme)
        }
    }
}