package com.vnteam.talktoai.data.sdk

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GoogleAuthHandler {
    actual suspend fun signIn(): String? {
        return null
    }

    actual fun signOut() {
    }

    actual fun getToken(): String? {
        TODO("Not yet implemented")
    }
}