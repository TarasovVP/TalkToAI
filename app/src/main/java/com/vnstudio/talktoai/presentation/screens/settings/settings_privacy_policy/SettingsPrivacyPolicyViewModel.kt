package com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.SettingsPrivacyPolicyUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsPrivacyPolicyViewModel @Inject constructor(
    private val application: Application,
    private val settingsPrivacyPolicyUseCase: SettingsPrivacyPolicyUseCase,
) : BaseViewModel(application) {

    val appLanguageLiveData = MutableLiveData<String>()
    val privacyPolicyLiveData = MutableLiveData<String>()

    fun getAppLanguage() {
        launch {
            settingsPrivacyPolicyUseCase.getAppLanguage().collect { appLang ->
                appLanguageLiveData.postValue(appLang ?: Locale.getDefault().language)
            }
        }
    }

    fun getPrivacyPolicy(appLang: String) {
        showProgress()
        launch {
            settingsPrivacyPolicyUseCase.getPrivacyPolicy(appLang) { operationResult ->
                when (operationResult) {
                    is Result.Success -> privacyPolicyLiveData.postValue(
                        operationResult.data ?: application.getString(
                            R.string.privacy_policy
                        )
                    )
                    is Result.Failure -> exceptionLiveData.postValue(operationResult.errorMessage.orEmpty())
                }
            }
            hideProgress()
        }
    }
}