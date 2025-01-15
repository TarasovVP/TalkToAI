package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.preferences

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserLoginUseCase(private val preferencesRepository: PreferencesRepository) {

    fun getUserLogin(): Flow<Result<String?>> {
        return preferencesRepository.getUserLogin().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }

    suspend fun setUserLogin(userLogin: String) {
        preferencesRepository.setUserLogin(userLogin)
    }
}