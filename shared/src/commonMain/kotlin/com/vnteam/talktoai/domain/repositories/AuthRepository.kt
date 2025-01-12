package com.vnteam.talktoai.domain.repositories

import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    fun addAuthStateListener()

    fun removeAuthStateListener()

    fun fetchSignInMethodsForEmail(email: String): Flow<List<String>>

    fun resetPassword(email: String): Flow<Unit>

    fun signInWithEmailAndPassword(email: String, password: String): Flow<String>

    fun signInWithGoogle(idToken: String): Flow<String>

    fun signInAnonymously(): Flow<String>

    fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<List<String>>

    fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Flow<Unit>

    fun reAuthenticate(): Flow<Unit>

    fun deleteUser(): Flow<Unit>

    fun signOut(): Flow<Unit>

    fun isGoogleAuthUser(): Boolean

    fun googleSignOut()

    fun googleSignIn()
}