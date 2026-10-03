package com.vnteam.talktoai.data.filestorage

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ImageStorage {
    suspend fun save(base64Data: String, mimeType: String): String?
    suspend fun load(storageKey: String): String?
    suspend fun delete(storageKey: String)
    suspend fun deleteAll(storageKeys: List<String>)
    fun pathFor(storageKey: String): String?
}

internal fun extensionFor(mimeType: String): String = when (mimeType) {
    "image/jpeg" -> "jpg"
    "image/png" -> "png"
    "image/gif" -> "gif"
    "image/webp" -> "webp"
    else -> "bin"
}
