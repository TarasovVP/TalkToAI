package com.vnstudio.talktoai.presentation.screens.settings.settings_list

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.CommonExtensions.isNetworkAvailable
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.usecases.SettingsListUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsListViewModel @Inject constructor(
    private val application: Application,
    private val settingsListUseCase: SettingsListUseCase
) : BaseViewModel(application) {

    val appLanguageLiveData = MutableLiveData<String>()
    val successFeedbackLiveData = MutableLiveData<Unit>()

    fun getAppLanguage() {
        launch {
            settingsListUseCase.getAppLanguage().collect { appLang ->
                appLanguageLiveData.postValue(appLang ?: Locale.getDefault().language)
            }
        }
    }

    fun currentUserEmail(): String {
        return settingsListUseCase.currentUserEmail()
    }

    fun insertFeedback(feedback: Feedback) {
        if (application.isNetworkAvailable()) {
            showProgress()
            settingsListUseCase.insertFeedback(feedback) { result ->
                when (result) {
                    is Result.Success -> successFeedbackLiveData.postValue(Unit)
                    is Result.Failure -> exceptionLiveData.postValue(result.errorMessage.orEmpty())
                }
            }
            hideProgress()
        } else {
            exceptionLiveData.postValue(application.getString(R.string.app_network_unavailable_repeat))
        }
    }
}