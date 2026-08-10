package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import kotlinx.coroutines.flow.firstOrNull

class SyncRemoteSettingsUseCase(
    private val remoteStoreRepository: RemoteStoreRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    suspend fun execute() {
        val result = remoteStoreRepository.getRemoteSettings().firstOrNull()
        if (result is Result.Success) {
            val settings = result.data ?: return
            settings["aiModel"]?.takeIf { it.isNotEmpty() }
                ?.let { preferencesRepository.setAiModel(it) }
            settings["apiKey"]?.takeIf { it.isNotEmpty() }
                ?.let { preferencesRepository.setApiKey(it) }
            settings["temperature"]?.takeIf { it.isNotEmpty() }?.let {
                preferencesRepository.setTemperature(it)
            }
            settings["globalContext"]?.let { preferencesRepository.setGlobalSystemContext(it) }
        }
    }
}
