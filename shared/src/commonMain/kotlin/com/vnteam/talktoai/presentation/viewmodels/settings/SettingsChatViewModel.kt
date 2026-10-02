package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.enums.ModelTier
import com.vnteam.talktoai.domain.models.parseTier
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.SyncRemoteSettingsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiTierUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiProviderUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.GlobalContextUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
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
    private val aiTierUseCase: AiTierUseCase,
    private val aiProviderUseCase: AiProviderUseCase,
    private val globalContextUseCase: GlobalContextUseCase,
    private val remoteStoreRepository: RemoteStoreRepository,
    private val syncRemoteSettingsUseCase: SyncRemoteSettingsUseCase,
) : BaseViewModel() {

    private val _aiProvider = MutableStateFlow(AiProviderType.OPENAI)
    val aiProvider = _aiProvider.asStateFlow()

    private val _aiTier = MutableStateFlow(ModelTier.BALANCED)
    val aiTier = _aiTier.asStateFlow()

    private val _settingsSaved = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val settingsSaved = _settingsSaved.asSharedFlow()

    private val _hasChanges = MutableStateFlow(false)
    val hasChanges = _hasChanges.asStateFlow()

    private val _globalContext = MutableStateFlow(String.EMPTY)
    val globalContext = _globalContext.asStateFlow()

    private var initialAiProvider = AiProviderType.OPENAI
    private var initialAiTier = ModelTier.BALANCED
    private var initialGlobalContext = String.EMPTY

    init {
        loadSettings()
    }

    private fun updateHasChanges() {
        _hasChanges.value = _aiProvider.value != initialAiProvider ||
                _aiTier.value != initialAiTier ||
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
                    updateHasChanges()
                }
            }
        }
        launchWithErrorHandling {
            aiTierUseCase.get().collect { result ->
                if (result is Result.Success) {
                    val saved = result.data?.takeIf { it.isNotEmpty() } ?: return@collect
                    val tier = parseTier(saved)
                    _aiTier.value = tier
                    initialAiTier = tier
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
        updateHasChanges()
    }

    fun onTierSelected(tier: ModelTier) {
        _aiTier.value = tier
        updateHasChanges()
    }

    fun onGlobalContextChanged(context: String) {
        _globalContext.value = context
        updateHasChanges()
    }

    fun saveSettings() {
        launchWithErrorHandling {
            aiProviderUseCase.set(_aiProvider.value.name)
            aiTierUseCase.set(_aiTier.value.name)
            globalContextUseCase.set(_globalContext.value)
            val remoteResult = remoteStoreRepository.setRemoteSettings(
                mapOf(
                    FirestoreConstants.FIELD_AI_PROVIDER to _aiProvider.value.name,
                    FirestoreConstants.FIELD_AI_TIER to _aiTier.value.name,
                    "globalContext" to _globalContext.value,
                )
            ).firstOrNull()
            if (remoteResult is Result.Failure &&
                remoteResult.errorMessage != "Not authenticated"
            ) {
                remoteResult.errorMessage?.takeIf { it.isNotEmpty() }?.let { showMessage(it) }
            }
            initialAiProvider = _aiProvider.value
            initialAiTier = _aiTier.value
            initialGlobalContext = _globalContext.value
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
