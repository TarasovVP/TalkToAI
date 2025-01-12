package com.vnteam.talktoai.presentation.viewmodels

import androidx.compose.ui.text.intl.Locale
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.LanguageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsLanguageViewModel(
    private val languageUseCase: LanguageUseCase,
) : BaseViewModel() {

    private val _appLanguage = MutableStateFlow(String.EMPTY)
    val appLanguage = _appLanguage.asStateFlow()

    fun getAppLanguage() {
        launchWithResultHandling {
            languageUseCase.getLanguage().onSuccess { appLang ->
                _appLanguage.value = appLang ?: Locale.current.language
            }
        }
    }

    fun setAppLanguage(appLang: String) {
        launchWithErrorHandling {
            languageUseCase.setLanguage(appLang)
            _appLanguage.value = appLang
        }
    }
}