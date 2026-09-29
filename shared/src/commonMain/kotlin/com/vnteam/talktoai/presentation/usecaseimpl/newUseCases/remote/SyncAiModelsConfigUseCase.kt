package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.aimodels.AiModelsCacheCodec
import com.vnteam.talktoai.domain.aimodels.AiModelsConfigResult
import com.vnteam.talktoai.domain.aimodels.AiModelsConfigValidator
import com.vnteam.talktoai.domain.models.AiModels
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Clock

class SyncAiModelsConfigUseCase(
    private val remoteStoreRepository: RemoteStoreRepository,
    private val preferencesRepository: PreferencesRepository,
    private val now: () -> Long = { Clock.System.now().toEpochMilliseconds() },
    private val log: (String) -> Unit = { println("AiModelsConfig: $it") },
) {

    suspend fun applyCached() {
        loadValidCache()?.let { AiModels.replace(it.models) }
    }

    suspend fun refreshIfDue() {
        try {
            val result = remoteStoreRepository.getAiModelsConfig().firstOrNull()
            if (result !is Result.Success || result.data == null) {
                log("fetch failed: ${(result as? Result.Failure)?.errorMessage}")
                return
            }
            when (val validated = AiModelsConfigValidator.validate(result.data!!)) {
                is AiModelsConfigResult.Rejected -> log("remote config rejected: ${validated.reason}")
                is AiModelsConfigResult.Valid -> {
                    validated.warnings.forEach(log)
                    preferencesRepository.setAiModelsCache(
                        AiModelsCacheCodec.encode(validated.schemaVersion, validated.models)
                    )
                    preferencesRepository.setAiModelsFetchedAt(now().toString())
                    AiModels.replace(validated.models)
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            log("refresh failed: ${e.message}")
        }
    }

    private suspend fun loadValidCache(): AiModelsConfigResult.Valid? {
        val cached = preferencesRepository.getAiModelsCache().firstOrNull()?.takeIf { it.isNotEmpty() } ?: return null
        val raw = AiModelsCacheCodec.decode(cached)
        if (raw == null) {
            log("cache is unreadable, ignoring")
            return null
        }
        return when (val validated = AiModelsConfigValidator.validate(raw)) {
            is AiModelsConfigResult.Valid -> validated
            is AiModelsConfigResult.Rejected -> {
                log("cache ignored: ${validated.reason}")
                null
            }
        }
    }
}
