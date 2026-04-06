package com.vnteam.talktoai.utils

import androidx.activity.ComponentActivity
import com.vnteam.talktoai.data.sdk.GoogleAuthHandler

class GoogleSignInInitializer(private val googleAuthHandler: GoogleAuthHandler) {
    fun initialize(activity: ComponentActivity) {
        googleAuthHandler.setActivity(activity)
    }
}
