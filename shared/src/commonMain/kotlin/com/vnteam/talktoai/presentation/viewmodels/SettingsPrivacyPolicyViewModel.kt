package com.vnteam.talktoai.presentation.viewmodels

import androidx.compose.ui.text.intl.Locale
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants.PRIVACY_POLICY
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.preferences.GetPrivacyPolicyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.preferences.LanguageUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsPrivacyPolicyViewModel(
    private val languageUseCase: LanguageUseCase,
    private val getPrivacyPolicyUseCase: GetPrivacyPolicyUseCase
) : BaseViewModel() {

    val appLanguageLiveData = MutableStateFlow(String.EMPTY)
    val privacyPolicyLiveData = MutableStateFlow(String.EMPTY)

    fun getAppLanguage() {
        launchWithResultHandling {
            languageUseCase.get().onSuccess { appLang ->
                appLanguageLiveData.value = appLang ?: Locale.current.language
            }
        }
    }

    fun getPrivacyPolicy(appLang: String) {
        showProgress()
        launchWithResultHandling {
            getPrivacyPolicyUseCase.execute(appLang).onSuccess { result ->
                privacyPolicyLiveData.value = result ?: PRIVACY_POLICY
            }
        }
    }
}