package com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy

import android.app.Application
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsPrivacyPolicyUseCase
import com.vnstudio.talktoai.infrastructure.Constants.PRIVACY_POLICY
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

class SettingsPrivacyPolicyViewModel(
    application: Application,
    private val settingsPrivacyPolicyUseCase: SettingsPrivacyPolicyUseCase,
) : BaseViewModel(application) {

    val appLanguageLiveData = MutableStateFlow(String.EMPTY)
    val privacyPolicyLiveData = MutableStateFlow(String.EMPTY)

    fun getAppLanguage() {
        launch {
            settingsPrivacyPolicyUseCase.getAppLanguage().collect { appLang ->
                appLanguageLiveData.value = appLang ?: Locale.getDefault().language
            }
        }
    }

    fun getPrivacyPolicy(appLang: String) {
        showProgress()
        launch {
            settingsPrivacyPolicyUseCase.getPrivacyPolicy(appLang) { operationResult ->
                when (operationResult) {
                    is Result.Success -> privacyPolicyLiveData.value =
                        operationResult.data ?: PRIVACY_POLICY

                    is Result.Failure -> exceptionLiveData.value =
                        operationResult.errorMessage.orEmpty()
                }
            }
            hideProgress()
        }
    }
}