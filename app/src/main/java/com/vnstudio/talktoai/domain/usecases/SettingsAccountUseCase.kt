package com.vnstudio.talktoai.domain.usecases

import com.google.firebase.auth.AuthCredential
import com.vnstudio.talktoai.domain.sealed_classes.Result

interface SettingsAccountUseCase {

    fun isLoggedInUser(): Boolean

    fun isAuthorisedUser(): Boolean

    fun isGoogleAuthUser(): Boolean

    fun currentUserEmail(): String

    fun signOut(result: (Result<Unit>) -> Unit)

    fun changePassword(currentPassword: String, newPassword: String, result: (Result<Unit>) -> Unit)

    fun reAuthenticate(authCredential: AuthCredential, result: (Result<Unit>) -> Unit)

    fun deleteUser(result: (Result<Unit>) -> Unit)

    suspend fun clearDataByKeys(keys: List<String>)

    suspend fun clearDataInDB()
}