package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_AI_MODEL
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_GLOBAL_CONTEXT
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_TEMPERATURE
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
            settings[FIELD_AI_MODEL]?.takeIf { it.isNotEmpty() }
                ?.let { preferencesRepository.setAiModel(it) }
            settings[FIELD_TEMPERATURE]?.takeIf { it.isNotEmpty() }?.let {
                preferencesRepository.setTemperature(it)
            }
            settings[FIELD_GLOBAL_CONTEXT]?.let { preferencesRepository.setGlobalSystemContext(it) }
        }
    }
}
