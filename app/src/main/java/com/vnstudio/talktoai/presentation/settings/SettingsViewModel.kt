package com.vnstudio.talktoai.presentation.settings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vnstudio.talktoai.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(application: Application, private val settingsUseCase: SettingsUseCaseImpl) : BaseViewModel(application) {

    val settingsLiveData = MutableLiveData<Unit>()

    fun changeSettings() {
        showProgress()
        viewModelScope.launch {
            val result = settingsUseCase.changeSettings()
            settingsLiveData.postValue(result)
            hideProgress()
        }
    }
}