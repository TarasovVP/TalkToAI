package com.vnstudio.talktoai.domain.usecases

import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.AuthCredential
import com.vnstudio.talktoai.data.network.Result

interface SettingsAccountUseCase {

    fun signOut(result: (Result<Unit>) -> Unit)

    fun changePassword(currentPassword: String, newPassword: String, result: (Result<Unit>) -> Unit)

    fun reAuthenticate(authCredential: AuthCredential, result: (Result<Unit>) -> Unit)

    fun deleteUser(result: (Result<Unit>) -> Unit)

    suspend fun clearDataByKeys(keys: List<Preferences.Key<*>>)
}