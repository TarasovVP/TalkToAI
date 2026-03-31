package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings

import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.DataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class TemperatureUseCase(private val preferencesRepository: PreferencesRepository) :
    DataUseCase<Float, Flow<Result<Float>>> {

    override fun get(): Flow<Result<Float>> {
        return preferencesRepository.getTemperature().map { value ->
            Result.Success(value?.toFloatOrNull() ?: SettingsConstants.AI_TEMPERATURE_DEFAULT)
        }.catch {
            emit(Result.Success(SettingsConstants.AI_TEMPERATURE_DEFAULT))
        }
    }

    override suspend fun set(params: Float) {
        preferencesRepository.setTemperature(params.toString())
    }

}
