package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.preferences

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ThemeUseCase(private val preferencesRepository: PreferencesRepository) {

    fun getIsDarkTheme(): Flow<Result<Boolean>> {
        return preferencesRepository.getIsDarkTheme().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }

    suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        preferencesRepository.setIsDarkTheme(isDarkTheme)
    }
}