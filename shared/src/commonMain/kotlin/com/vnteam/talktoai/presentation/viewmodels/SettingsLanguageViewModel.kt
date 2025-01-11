package com.vnteam.talktoai.presentation.viewmodels

import androidx.compose.ui.text.intl.Locale
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.usecase.SettingsLanguageUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsLanguageViewModel(
    private val settingsLanguageUseCase: SettingsLanguageUseCase,
) : BaseViewModel() {

    val appLanguageLiveData = MutableStateFlow(String.EMPTY)


    fun getAppLanguage() {
        launchWithResultHandling {
            settingsLanguageUseCase.getAppLanguage().onSuccess { appLang ->
                appLanguageLiveData.value = appLang ?: Locale.current.language
            }
        }
    }

    fun setAppLanguage(appLang: String) {
        launchWithErrorHandling {
            settingsLanguageUseCase.setAppLanguage(appLang)
            appLanguageLiveData.value = appLang
        }
    }
}