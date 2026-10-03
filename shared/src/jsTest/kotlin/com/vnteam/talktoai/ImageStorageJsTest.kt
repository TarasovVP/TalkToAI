package com.vnteam.talktoai

import com.vnteam.talktoai.data.filestorage.ImageStorage
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNull

class ImageStorageJsTest {

    private val storage = ImageStorage()

    @Test
    fun saveReturnsNullOnJsStub() = runTest {
        assertNull(storage.save("data", "image/png"))
    }

    @Test
    fun loadReturnsNullOnJsStub() = runTest {
        assertNull(storage.load("key.png"))
    }

    @Test
    fun pathForReturnsNullOnJsStub() {
        assertNull(storage.pathFor("key.png"))
    }
}
