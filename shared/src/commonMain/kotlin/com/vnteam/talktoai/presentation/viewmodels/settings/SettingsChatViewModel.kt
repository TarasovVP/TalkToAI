package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiModelUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ApiKeyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsChatViewModel(
    private val onboardingUseCase: OnboardingUseCase,
    private val userEmailUseCase: UserEmailUseCase,
    private val aiModelUseCase: AiModelUseCase,
    private val apiKeyUseCase: ApiKeyUseCase,
) : BaseViewModel() {

    private val _aiModel = MutableStateFlow(Constants.AI_MODEL_DEFAULT)
    val aiModel = _aiModel.asStateFlow()

    private val _apiKey = MutableStateFlow(String.EMPTY)
    val apiKey = _apiKey.asStateFlow()

    private val _settingsSaved = MutableStateFlow(false)
    val settingsSaved = _settingsSaved.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        launchWithErrorHandling {
            aiModelUseCase.get().collect { result ->
                if (result is Result.Success && !result.data.isNullOrEmpty()) {
                    _aiModel.value = result.data!!
                }
            }
        }
        launchWithErrorHandling {
            apiKeyUseCase.get().collect { result ->
                if (result is Result.Success && result.data != null) {
                    _apiKey.value = result.data!!
                }
            }
        }
    }

    fun onModelSelected(model: String) {
        _aiModel.value = model
    }

    fun onApiKeyChanged(key: String) {
        _apiKey.value = key
    }

    fun saveSettings() {
        launchWithErrorHandling {
            aiModelUseCase.set(_aiModel.value)
            apiKeyUseCase.set(_apiKey.value)
            _settingsSaved.value = true
            _settingsSaved.value = false
        }
    }

    fun clearUserData() {
        launchWithErrorHandling {
            onboardingUseCase.set(false)
            userEmailUseCase.set(String.EMPTY)
        }
    }
}
