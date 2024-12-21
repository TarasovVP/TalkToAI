package com.vnteam.talktoai.data

import platform.Foundation.NSUUID

actual fun generateUUID(): String = NSUUID().UUIDString()

actual fun baseUrl(): String = BASE_URL
actual fun apiKey(): String = API_KEY
actual fun organizationId(): String = ORGANIZATION_ID
actual fun projectId(): String = PROJECT_ID