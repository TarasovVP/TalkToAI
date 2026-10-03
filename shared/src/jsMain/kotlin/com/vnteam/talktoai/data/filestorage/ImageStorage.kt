package com.vnteam.talktoai.data.filestorage

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ImageStorage {

    actual suspend fun save(base64Data: String, mimeType: String): String? = null

    actual suspend fun load(storageKey: String): String? = null

    actual suspend fun delete(storageKey: String) = Unit

    actual suspend fun deleteAll(storageKeys: List<String>) = Unit

    actual fun pathFor(storageKey: String): String? = null
}
