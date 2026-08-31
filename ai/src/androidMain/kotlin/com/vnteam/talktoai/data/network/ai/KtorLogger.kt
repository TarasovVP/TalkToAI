package com.vnteam.talktoai.data.network.ai

import android.util.Log
import com.vnteam.talktoai.ai.BuildConfig
import io.ktor.client.plugins.logging.Logger

actual fun platformLogger(): Logger = object : Logger {
    override fun log(message: String) {
        Log.d("Ktor", message)
    }
}

actual val isDebug: Boolean = BuildConfig.DEBUG
