package com.vnteam.talktoai.data

actual fun generateUUID(): String = js("crypto.randomUUID().toString()").unsafeCast<String>()

actual fun baseUrl(): String = CLOUD_URL