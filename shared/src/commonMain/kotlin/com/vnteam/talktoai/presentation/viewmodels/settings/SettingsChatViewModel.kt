package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.AiModel
import com.vnteam.talktoai.domain.models.AiModels
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.SyncRemoteSettingsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiModelUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiProviderUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AnthropicApiKeyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ApiKeyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.GlobalContextUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.TemperatureUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull

class SettingsChatViewModel(
    private val onboardingUseCase: OnboardingUseCase,
    private val userEmailUseCase: UserEmailUseCase,
    private val aiModelUseCase: AiModelUseCase,
    private val aiProviderUseCase: AiProviderUseCase,
    private val apiKeyUseCase: ApiKeyUseCase,
    private val anthropicApiKeyUseCase: AnthropicApiKeyUseCase,
    private val temperatureUseCase: TemperatureUseCase,
    private val globalContextUseCase: GlobalContextUseCase,
    private val remoteStoreRepository: RemoteStoreRepository,
    private val syncRemoteSettingsUseCase: SyncRemoteSettingsUseCase,
) : BaseViewModel() {

    private val _aiProvider = MutableStateFlow(AiProviderType.OPENAI)
    val aiProvider = _aiProvider.asStateFlow()

    private val _aiModel = MutableStateFlow(SettingsConstants.OPENAI_AI_MODEL_DEFAULT)
    val aiModel = _aiModel.asStateFlow()

    private val _apiKey = MutableStateFlow(String.EMPTY)
    val apiKey = _apiKey.asStateFlow()

    private val _anthropicApiKey = MutableStateFlow(String.EMPTY)
    val anthropicApiKey = _anthropicApiKey.asStateFlow()

    private val _temperature = MutableStateFlow(SettingsConstants.AI_TEMPERATURE_DEFAULT)
    val temperature = _temperature.asStateFlow()

    private val _settingsSaved = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val settingsSaved = _settingsSaved.asSharedFlow()

    private val _availableModels = MutableStateFlow<List<AiModel>>(AiModels.OPENAI)
    val availableModels = _availableModels.asStateFlow()

    private val _hasChanges = MutableStateFlow(false)
    val hasChanges = _hasChanges.asStateFlow()

    private val _savedApiKey = MutableStateFlow(String.EMPTY)
    val savedApiKey = _savedApiKey.asStateFlow()

    private val _savedAnthropicApiKey = MutableStateFlow(String.EMPTY)
    val savedAnthropicApiKey = _savedAnthropicApiKey.asStateFlow()

    private val _globalContext = MutableStateFlow(String.EMPTY)
    val globalContext = _globalContext.asStateFlow()

    private var initialAiProvider = AiProviderType.OPENAI
    private var initialAiModel = SettingsConstants.OPENAI_AI_MODEL_DEFAULT
    private var initialApiKey = String.EMPTY
    private var initialAnthropicApiKey = String.EMPTY
    private var initialTemperature = SettingsConstants.AI_TEMPERATURE_DEFAULT
    private var initialGlobalContext = String.EMPTY

    init {
        loadSettings()
    }

    private fun updateHasChanges() {
        _hasChanges.value = _aiProvider.value != initialAiProvider ||
                _aiModel.value != initialAiModel ||
                _apiKey.value != initialApiKey ||
                _anthropicApiKey.value != initialAnthropicApiKey ||
                _temperature.value != initialTemperature ||
                _globalContext.value != initialGlobalContext
    }

    private fun loadSettings() {
        launchWithErrorHandling {
            syncRemoteSettingsUseCase.execute()
        }
        launchWithErrorHandling {
            aiProviderUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val saved = result.data?.takeIf { it.isNotEmpty() } ?: return@collect
                    val provider = runCatching { AiProviderType.valueOf(saved) }.getOrNull() ?: AiProviderType.OPENAI
                    _aiProvider.value = provider
                    initialAiProvider = provider
                    _availableModels.value = AiModels.forProvider(provider)
                    updateHasChanges()
                }
            }
        }
        launchWithErrorHandling {
            aiModelUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val saved = result.data?.takeIf { it.isNotEmpty() } ?: return@collect
                    val providerModels = AiModels.forProvider(_aiProvider.value)
                    val validated = if (providerModels.any { it.id == saved }) saved
                    else AiModels.balancedFor(_aiProvider.value).id
                    _aiModel.value = validated
                    initialAiModel = validated
                    updateHasChanges()
                }
            }
        }
        launchWithErrorHandling {
            apiKeyUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val key = result.data.orEmpty()
                    _apiKey.value = key
                    initialApiKey = key
                    _savedApiKey.value = key
                    updateHasChanges()
                }
            }
        }
        launchWithErrorHandling {
            anthropicApiKeyUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val key = result.data.orEmpty()
                    _anthropicApiKey.value = key
                    initialAnthropicApiKey = key
                    _savedAnthropicApiKey.value = key
                    updateHasChanges()
                }
            }
        }
        launchWithErrorHandling {
            temperatureUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val temp = result.data ?: SettingsConstants.AI_TEMPERATURE_DEFAULT
                    _temperature.value = temp
                    initialTemperature = temp
                    updateHasChanges()
                }
            }
        }
        launchWithErrorHandling {
            globalContextUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val ctx = result.data.orEmpty()
                    _globalContext.value = ctx
                    initialGlobalContext = ctx
                    updateHasChanges()
                }
            }
        }
    }

    fun onProviderSelected(provider: AiProviderType) {
        _aiProvider.value = provider
        _availableModels.value = AiModels.forProvider(provider)
        _aiModel.value = AiModels.balancedFor(provider).id
        updateHasChanges()
    }

    fun onModelSelected(model: String) {
        _aiModel.value = model
        updateHasChanges()
    }

    fun onApiKeyChanged(key: String) {
        _apiKey.value = key
        updateHasChanges()
    }

    fun onAnthropicApiKeyChanged(key: String) {
        _anthropicApiKey.value = key
        updateHasChanges()
    }

    fun onTemperatureChanged(temperature: Float) {
        _temperature.value = (temperature * 10).toInt() / 10f
        updateHasChanges()
    }

    fun onGlobalContextChanged(context: String) {
        _globalContext.value = context
        updateHasChanges()
    }

    fun saveSettings() {
        launchWithErrorHandling {
            aiProviderUseCase.set(_aiProvider.value.name)
            aiModelUseCase.set(_aiModel.value)
            apiKeyUseCase.set(_apiKey.value)
            anthropicApiKeyUseCase.set(_anthropicApiKey.value)
            temperatureUseCase.set(_temperature.value)
            globalContextUseCase.set(_globalContext.value)
            val remoteResult = remoteStoreRepository.setRemoteSettings(
                mapOf(
                    "aiModel" to _aiModel.value,
                    "apiKey" to _apiKey.value,
                    "temperature" to _temperature.value.toString(),
                    "globalContext" to _globalContext.value,
                )
            ).firstOrNull()
            if (remoteResult is Result.Failure &&
                remoteResult.errorMessage != "Not authenticated"
            ) {
                remoteResult.errorMessage?.takeIf { it.isNotEmpty() }?.let { showMessage(it) }
            }
            initialAiProvider = _aiProvider.value
            initialAiModel = _aiModel.value
            initialApiKey = _apiKey.value
            initialAnthropicApiKey = _anthropicApiKey.value
            initialTemperature = _temperature.value
            initialGlobalContext = _globalContext.value
            _savedApiKey.value = _apiKey.value
            _savedAnthropicApiKey.value = _anthropicApiKey.value
            _hasChanges.value = false
            _settingsSaved.emit(Unit)
        }
    }

    fun clearApiKey() {
        launchWithErrorHandling {
            apiKeyUseCase.set(String.EMPTY)
            _apiKey.value = String.EMPTY
            initialApiKey = String.EMPTY
            _savedApiKey.value = String.EMPTY
            updateHasChanges()
        }
    }

    fun clearAnthropicApiKey() {
        launchWithErrorHandling {
            anthropicApiKeyUseCase.set(String.EMPTY)
            _anthropicApiKey.value = String.EMPTY
            initialAnthropicApiKey = String.EMPTY
            _savedAnthropicApiKey.value = String.EMPTY
            updateHasChanges()
        }
    }

    fun clearUserData() {
        launchWithErrorHandling {
            onboardingUseCase.set(false)
            userEmailUseCase.set(String.EMPTY)
        }
    }
}
