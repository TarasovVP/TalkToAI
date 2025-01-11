package com.vnteam.talktoai.presentation.viewmodels

import androidx.compose.ui.text.intl.Locale
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants.PRIVACY_POLICY
import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.usecase.SettingsPrivacyPolicyUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsPrivacyPolicyViewModel(
    private val settingsPrivacyPolicyUseCase: SettingsPrivacyPolicyUseCase,
) : BaseViewModel() {

    val appLanguageLiveData = MutableStateFlow(String.EMPTY)
    val privacyPolicyLiveData = MutableStateFlow(String.EMPTY)

    fun getAppLanguage() {
        launchWithConditions {
            settingsPrivacyPolicyUseCase.getAppLanguage().collect { appLang ->
                appLanguageLiveData.value = appLang ?: Locale.current.language
            }
        }
    }

    fun getPrivacyPolicy(appLang: String) {
        showProgress()
        launchWithConditions {
            settingsPrivacyPolicyUseCase.getPrivacyPolicy(appLang) { operationResult ->
                /*when (operationResult) {
                    is NetworkResult.Success -> privacyPolicyLiveData.value =
                        operationResult.data ?: PRIVACY_POLICY

                    is NetworkResult.Failure -> _exceptionMessage.value =
                        operationResult.errorMessage.orEmpty()
                }*/
            }
            hideProgress()
        }
    }
}