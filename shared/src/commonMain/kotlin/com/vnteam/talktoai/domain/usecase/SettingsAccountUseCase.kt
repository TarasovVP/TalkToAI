package com.vnteam.talktoai.domain.usecase

import kotlinx.coroutines.flow.Flow


interface SettingsAccountUseCase {

    fun signOut(): Flow<Unit>

    fun deleteUser(): Flow<Unit>

    suspend fun clearDataByKeys(keys: List<String>)

    suspend fun clearDataInDB()
}