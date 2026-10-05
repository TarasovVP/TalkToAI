package com.vnteam.talktoai

import com.vnteam.talktoai.data.filestorage.ImageCompressor
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNull

class ImageCompressorJsTest {

    private val compressor = ImageCompressor()

    @Test
    fun makeThumbnailReturnsNullForGarbageInput() = runTest {
        assertNull(compressor.makeThumbnail("not a real image", "image/png"))
    }
}
