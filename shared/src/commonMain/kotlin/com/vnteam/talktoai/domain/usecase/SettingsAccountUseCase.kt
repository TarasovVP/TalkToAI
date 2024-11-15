package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult


interface SettingsAccountUseCase {

    fun isLoggedInUser(): Boolean

    fun isAuthorisedUser(): Boolean

    fun isGoogleAuthUser(): Boolean

    fun currentUserEmail(): String

    fun signOut(result: (NetworkResult<Unit>) -> Unit)

    fun changePassword(currentPassword: String, newPassword: String, result: (NetworkResult<Unit>) -> Unit)

    fun reAuthenticate(/*authCredential: AuthCredential, */result: (NetworkResult<Unit>) -> Unit)

    fun deleteUser(result: (NetworkResult<Unit>) -> Unit)

    suspend fun clearDataByKeys(keys: List<String>)

    suspend fun clearDataInDB()
}