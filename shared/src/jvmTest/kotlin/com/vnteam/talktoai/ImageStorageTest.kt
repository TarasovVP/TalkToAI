package com.vnteam.talktoai

import com.vnteam.talktoai.data.filestorage.ImageStorage
import kotlinx.coroutines.test.runTest
import java.nio.file.Files
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalEncodingApi::class)
class ImageStorageTest {

    private val tempDir = Files.createTempDirectory("image-storage-test").toFile()
    private val storage = ImageStorage(tempDir)

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun saveReturnsAKeyAndLoadReturnsTheSameBase64() = runTest {
        val original = Base64.encode("hello image bytes".encodeToByteArray())
        val key = storage.save(original, "image/png")
        assertNotNull(key)
        assertEquals(original, storage.load(key))
    }

    @Test
    fun deleteRemovesTheFile() = runTest {
        val key = storage.save(Base64.encode("x".encodeToByteArray()), "image/jpeg")!!
        storage.delete(key)
        assertNull(storage.load(key))
        assertNull(storage.pathFor(key))
    }

    @Test
    fun deleteAllRemovesMultipleFiles() = runTest {
        val key1 = storage.save(Base64.encode("a".encodeToByteArray()), "image/png")!!
        val key2 = storage.save(Base64.encode("b".encodeToByteArray()), "image/gif")!!
        storage.deleteAll(listOf(key1, key2))
        assertNull(storage.pathFor(key1))
        assertNull(storage.pathFor(key2))
    }

    @Test
    fun pathForReturnsNullForAnUnknownKey() {
        assertNull(storage.pathFor("does-not-exist.png"))
    }
}
