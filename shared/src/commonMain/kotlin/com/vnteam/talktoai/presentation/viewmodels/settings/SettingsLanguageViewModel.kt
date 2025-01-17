package com.vnteam.talktoai.presentation.viewmodels.settings

import androidx.compose.ui.text.intl.Locale
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.LanguageUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsLanguageViewModel(
    private val languageUseCase: LanguageUseCase,
) : BaseViewModel() {

    private val _appLanguage = MutableStateFlow(String.EMPTY)
    val appLanguage = _appLanguage.asStateFlow()

    fun getAppLanguage() {
        launchWithResultHandling {
            languageUseCase.get().onSuccess { appLang ->
                _appLanguage.value = appLang ?: Locale.current.language
            }
        }
    }

    fun setAppLanguage(appLang: String) {
        launchWithErrorHandling {
            languageUseCase.set(appLang)
            _appLanguage.value = appLang
        }
    }
}