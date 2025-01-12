package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import kotlinx.coroutines.flow.Flow


interface SettingsAccountUseCase {

    fun isLoggedInUser(): Boolean

    fun isAuthorisedUser(): Boolean

    fun isGoogleAuthUser(): Boolean

    fun currentUserEmail(): String

    fun signOut(): Flow<Result<Unit>>

    fun changePassword(
        currentPassword: String,
        newPassword: String): Flow<Result<Unit>>

    fun reAuthenticate(/*authCredential: AuthCredential, */): Flow<Result<Unit>>

    fun deleteUser(): Flow<Result<Unit>>

    suspend fun clearDataByKeys(keys: List<String>)

    suspend fun clearDataInDB()

    fun googleSignIn()
}