package com.vnstudio.talktoai.presentation.screens.settings.settings_language

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.domain.usecases.SettingsLanguageUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import java.util.*



class SettingsLanguageViewModel(
    application: Application,
    private val settingsLanguageUseCase: SettingsLanguageUseCase,
) : BaseViewModel(application) {

    val appLanguageLiveData = MutableLiveData<String>()


    fun getAppLanguage() {
        launch {
            settingsLanguageUseCase.getAppLanguage().collect { appLang ->
                appLanguageLiveData.postValue(appLang ?: Locale.getDefault().language)
            }
        }
    }

    fun setAppLanguage(appLang: String) {
        launch {
            settingsLanguageUseCase.setAppLanguage(appLang)
            appLanguageLiveData.postValue(appLang)
        }
    }
}