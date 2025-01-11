package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow


interface SettingsAccountUseCase {

    fun isLoggedInUser(): Boolean

    fun isAuthorisedUser(): Boolean

    fun isGoogleAuthUser(): Boolean

    fun currentUserEmail(): String

    fun signOut(): Flow<NetworkResult<Unit>>

    fun changePassword(
        currentPassword: String,
        newPassword: String): Flow<NetworkResult<Unit>>

    fun reAuthenticate(/*authCredential: AuthCredential, */): Flow<NetworkResult<Unit>>

    fun deleteUser(): Flow<NetworkResult<Unit>>

    suspend fun clearDataByKeys(keys: List<String>)

    suspend fun clearDataInDB()

    fun googleSignIn()
}