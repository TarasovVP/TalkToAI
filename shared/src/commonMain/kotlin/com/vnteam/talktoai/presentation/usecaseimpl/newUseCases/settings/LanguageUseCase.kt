package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.DataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class LanguageUseCase(private val preferencesRepository: PreferencesRepository) :
    DataUseCase<String, Flow<Result<String?>>> {

    override fun get(): Flow<Result<String?>> {
        return preferencesRepository.getLanguage().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }

    override suspend fun set(params: String) {
        preferencesRepository.setLanguage(params)
    }
}