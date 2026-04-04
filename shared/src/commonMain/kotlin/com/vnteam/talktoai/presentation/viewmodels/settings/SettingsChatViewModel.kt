package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.ai.GetModelsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiModelUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ApiKeyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.TemperatureUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _settingsSaved = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val settingsSaved = _settingsSaved.asSharedFlow()

    private val _availableModels = MutableStateFlow(SettingsConstants.AI_MODELS)
    val availableModels = _availableModels.asStateFlow()

    private val _hasChanges = MutableStateFlow(false)
    val hasChanges = _hasChanges.asStateFlow()

    private var savedAiModel = SettingsConstants.AI_MODEL_DEFAULT
    private var savedApiKey = String.EMPTY
    private var savedTemperature = SettingsConstants.AI_TEMPERATURE_DEFAULT

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

    private fun updateHasChanges() {
        _hasChanges.value = _aiModel.value != savedAiModel ||
                _apiKey.value != savedApiKey ||
                _temperature.value != savedTemperature
    }

    private fun loadSettings() {
        launchWithErrorHandling {
            aiModelUseCase.get().collect { result ->
                if (result is Result.Success) {
                    result.data?.takeIf { it.isNotEmpty() }?.let {
                        _aiModel.value = it
                        savedAiModel = it
                        updateHasChanges()
                    }
                }
            }
        }
        launchWithErrorHandling {
            apiKeyUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val key = result.data.orEmpty()
                    _apiKey.value = key
                    savedApiKey = key
                    updateHasChanges()
                    loadModels(key)
                }
            }
        }
        launchWithErrorHandling {
            temperatureUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val temp = result.data ?: SettingsConstants.AI_TEMPERATURE_DEFAULT
                    _temperature.value = temp
                    savedTemperature = temp
                    updateHasChanges()
                }
            }
        }
    }

    fun onModelSelected(model: String) {
        _aiModel.value = model
        updateHasChanges()
    }

    fun onApiKeyChanged(key: String) {
        _apiKey.value = key
        updateHasChanges()
    }

    fun onTemperatureChanged(temperature: Float) {
        _temperature.value = (temperature * 10).toInt() / 10f
        updateHasChanges()
    }

    fun saveSettings() {
        launchWithErrorHandling {
            aiModelUseCase.set(_aiModel.value)
            apiKeyUseCase.set(_apiKey.value)
            temperatureUseCase.set(_temperature.value)
            savedAiModel = _aiModel.value
            savedApiKey = _apiKey.value
            savedTemperature = _temperature.value
            _hasChanges.value = false
            _settingsSaved.emit(Unit)
        }
    }

    fun clearUserData() {
        launchWithErrorHandling {
            onboardingUseCase.set(false)
            userEmailUseCase.set(String.EMPTY)
        }
    }
}
