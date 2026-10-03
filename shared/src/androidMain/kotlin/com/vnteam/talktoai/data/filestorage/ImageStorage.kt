package com.vnteam.talktoai.data.filestorage

import android.content.Context
import com.vnteam.talktoai.data.generateUUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ImageStorage(private val context: Context) {

    private val dir by lazy { File(context.filesDir, "images") }

    actual suspend fun save(base64Data: String, mimeType: String): String? = withContext(Dispatchers.IO) {
        runCatching {
            dir.mkdirs()
            val key = "${generateUUID()}.${extensionFor(mimeType)}"
            File(dir, key).writeBytes(Base64.decode(base64Data))
            key
        }.getOrNull()
    }

    actual suspend fun load(storageKey: String): String? = withContext(Dispatchers.IO) {
        runCatching {
            File(dir, storageKey).takeIf { it.exists() }?.readBytes()?.let { Base64.encode(it) }
        }.getOrNull()
    }

    actual suspend fun delete(storageKey: String) = withContext(Dispatchers.IO) {
        runCatching { File(dir, storageKey).delete() }
        Unit
    }

    actual suspend fun deleteAll(storageKeys: List<String>) = withContext(Dispatchers.IO) {
        storageKeys.forEach { runCatching { File(dir, it).delete() } }
    }

    actual fun pathFor(storageKey: String): String? =
        File(dir, storageKey).takeIf { it.exists() }?.absolutePath
}
