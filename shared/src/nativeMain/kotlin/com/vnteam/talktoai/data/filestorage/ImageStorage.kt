package com.vnteam.talktoai.data.filestorage

import com.vnteam.talktoai.data.generateUUID
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.create
import platform.Foundation.data
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ImageStorage {

    private fun imagesDirPath(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        val path = (documentDirectory?.path ?: "") + "/images"
        NSFileManager.defaultManager.createDirectoryAtPath(path, true, null, null)
        return path
    }

    actual suspend fun save(base64Data: String, mimeType: String): String? = runCatching {
        val data = NSData.create(base64EncodedString = base64Data, options = 0uL)
            ?: return@runCatching null
        val key = "${generateUUID()}.${extensionFor(mimeType)}"
        val ok = data.writeToFile("${imagesDirPath()}/$key", true)
        if (ok) key else null
    }.getOrNull()

    actual suspend fun load(storageKey: String): String? = runCatching {
        val path = "${imagesDirPath()}/$storageKey"
        if (!NSFileManager.defaultManager.fileExistsAtPath(path)) return@runCatching null
        NSData.create(contentsOfFile = path)
            ?.base64EncodedStringWithOptions(0uL)
    }.getOrNull()

    actual suspend fun delete(storageKey: String) {
        runCatching { NSFileManager.defaultManager.removeItemAtPath("${imagesDirPath()}/$storageKey", null) }
    }

    actual suspend fun deleteAll(storageKeys: List<String>) {
        storageKeys.forEach { delete(it) }
    }

    actual fun pathFor(storageKey: String): String? {
        val path = "${imagesDirPath()}/$storageKey"
        return path.takeIf { NSFileManager.defaultManager.fileExistsAtPath(it) }
    }
}
