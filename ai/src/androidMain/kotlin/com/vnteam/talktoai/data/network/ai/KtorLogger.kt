package com.vnteam.talktoai.data.network.ai

import android.util.Log
import io.ktor.client.plugins.logging.Logger

actual fun platformLogger(): Logger = object : Logger {
    override fun log(message: String) {
        Log.d("Ktor", message)
    }
}
