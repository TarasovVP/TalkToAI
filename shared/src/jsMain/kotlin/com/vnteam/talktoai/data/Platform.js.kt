package com.vnteam.talktoai.data

actual fun generateUUID(): String = js("crypto.randomUUID().toString()").unsafeCast<String>()

actual fun baseUrl(): String = BASE_URL
actual fun apiKey(): String  = API_KEY
actual fun organizationId(): String = ORGANIZATION_ID