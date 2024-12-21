package com.vnteam.talktoai.data

import java.util.UUID

actual fun generateUUID(): String = UUID.randomUUID().toString()

actual fun baseUrl(): String = BASE_URL
actual fun apiKey(): String = API_KEY
actual fun organizationId(): String = ORGANIZATION_ID
actual fun projectId(): String = PROJECT_ID