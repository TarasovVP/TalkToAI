package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserSessionUseCase(private val preferencesRepository: PreferencesRepository) {

    fun getIsLoggedInUser(): Flow<Result<Boolean?>> {
        return preferencesRepository.getIsLoggedInUser().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }

    suspend fun setLoggedInUser(isLoggedInUser: Boolean) {
        preferencesRepository.setLoggedInUser(isLoggedInUser)
    }
}