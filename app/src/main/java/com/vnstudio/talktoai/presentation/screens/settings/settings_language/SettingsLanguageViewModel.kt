package com.vnstudio.talktoai.presentation.screens.settings.settings_language

import android.app.Application
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.domain.usecases.SettingsLanguageUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class SettingsLanguageViewModel(
    application: Application,
    private val settingsLanguageUseCase: SettingsLanguageUseCase,
) : BaseViewModel(application) {

    val appLanguageLiveData = MutableStateFlow(String.EMPTY)


    fun getAppLanguage() {
        launch {
            settingsLanguageUseCase.getAppLanguage().collect { appLang ->
                appLanguageLiveData.value = appLang ?: Locale.getDefault().language
            }
        }
    }

    fun setAppLanguage(appLang: String) {
        launch {
            settingsLanguageUseCase.setAppLanguage(appLang)
            appLanguageLiveData.value = appLang
        }
    }
}