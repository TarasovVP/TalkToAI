package com.vnteam.talktoai.data.sdk

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class GoogleAuthHandler {
    suspend fun signIn(): String?
    fun signOut()
    fun getToken(): String?
}