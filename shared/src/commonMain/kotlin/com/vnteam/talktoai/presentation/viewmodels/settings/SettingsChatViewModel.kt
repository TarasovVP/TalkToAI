package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.ai.GetModelsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiModelUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ApiKeyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.TemperatureUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsChatViewModel(
    private val onboardingUseCase: OnboardingUseCase,
    private val userEmailUseCase: UserEmailUseCase,
    private val aiModelUseCase: AiModelUseCase,
    private val apiKeyUseCase: ApiKeyUseCase,
    private val getModelsUseCase: GetModelsUseCase,
    private val temperatureUseCase: TemperatureUseCase,
) : BaseViewModel() {

    private val _aiModel = MutableStateFlow(SettingsConstants.AI_MODEL_DEFAULT)
    val aiModel = _aiModel.asStateFlow()

    private val _apiKey = MutableStateFlow(String.EMPTY)
    val apiKey = _apiKey.asStateFlow()

    private val _temperature = MutableStateFlow(SettingsConstants.AI_TEMPERATURE_DEFAULT)
    val temperature = _temperature.asStateFlow()

    private val _settingsSaved = MutableStateFlow(false)
    val settingsSaved = _settingsSaved.asStateFlow()

    private val _availableModels = MutableStateFlow(SettingsConstants.AI_MODELS)
    val availableModels = _availableModels.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadModels(apiKey: String) {
        launchWithErrorHandling {
            getModelsUseCase.execute(apiKey.takeIf { it.isNotEmpty() }).collect { result ->
                if (result is Result.Success) {
                    val models = result.data?.data?.map { it.id }
                    if (!models.isNullOrEmpty()) {
                        _availableModels.value = models
                    }
                }
            }
        }
    }

    private fun loadSettings() {
        launchWithErrorHandling {
            aiModelUseCase.get().collect { result ->
                if (result is Result.Success) {
                    result.data?.takeIf { it.isNotEmpty() }?.let { _aiModel.value = it }
                }
            }
        }
        launchWithErrorHandling {
            apiKeyUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val key = result.data.orEmpty()
                    _apiKey.value = key
                    loadModels(key)
                }
            }
        }
        launchWithErrorHandling {
            temperatureUseCase.get().collect { result ->
                if (result is Result.Success) {
                    _temperature.value = result.data ?: SettingsConstants.AI_TEMPERATURE_DEFAULT
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

    fun onTemperatureChanged(temperature: Float) {
        _temperature.value = temperature
    }

    fun saveSettings() {
        launchWithErrorHandling {
            aiModelUseCase.set(_aiModel.value)
            apiKeyUseCase.set(_apiKey.value)
            temperatureUseCase.set(_temperature.value)
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
