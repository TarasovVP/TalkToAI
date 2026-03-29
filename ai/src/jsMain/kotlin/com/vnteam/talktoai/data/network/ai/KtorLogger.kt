package com.vnteam.talktoai.data.network.ai

import io.ktor.client.plugins.logging.Logger

actual fun platformLogger(): Logger = object : Logger {
    override fun log(message: String) {
        console.log("Ktor: $message")
    }
}
