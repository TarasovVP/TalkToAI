package com.vnteam.talktoai.domain.repositories


interface AuthRepository {

    fun addAuthStateListener()

    fun removeAuthStateListener()

    fun isLoggedInUser(): Boolean

    fun isAuthorisedUser(): Boolean

    fun isGoogleAuthUser(): Boolean

    fun currentUserEmail(): String

    fun sendPasswordResetEmail(email: String, result: (Result<Unit>) -> Unit)

    fun fetchSignInMethodsForEmail(email: String, result: (Result<List<String>>) -> Unit)

    fun signInWithEmailAndPassword(email: String, password: String, result: (Result<Unit>) -> Unit)

    fun signInWithGoogle(idToken: String, result: (Result<Unit>) -> Unit)

    fun signInAnonymously(result: (Result<Unit>) -> Unit)

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (Result<String>) -> Unit,
    )

    fun changePassword(currentPassword: String, newPassword: String, result: (Result<Unit>) -> Unit)

    fun reAuthenticate()

    fun deleteUser(result: (Result<Unit>) -> Unit)

    fun signOut(result: (Result<Unit>) -> Unit)
}