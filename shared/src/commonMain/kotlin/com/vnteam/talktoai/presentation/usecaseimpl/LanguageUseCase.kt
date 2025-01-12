package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class LanguageUseCase(private val preferencesRepository: PreferencesRepository) {

    fun getLanguage(): Flow<Result<String>> {
        return preferencesRepository.getLanguage().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }

    suspend fun setLanguage(language: String) {
        preferencesRepository.setLanguage(language)
    }
}