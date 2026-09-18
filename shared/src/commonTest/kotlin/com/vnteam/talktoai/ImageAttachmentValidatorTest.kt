package com.vnteam.talktoai

import com.vnteam.talktoai.domain.models.ALLOWED_IMAGE_MIME_TYPES
import com.vnteam.talktoai.domain.models.ImageValidationResult
import com.vnteam.talktoai.domain.models.MAX_IMAGE_SIZE_BYTES
import com.vnteam.talktoai.domain.models.PickedImage
import com.vnteam.talktoai.domain.models.validate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ImageAttachmentValidatorTest {

    private fun image(mimeType: String, sizeBytes: Long = 100L) =
        PickedImage(base64Data = "abc", mimeType = mimeType, sizeBytes = sizeBytes)

    @Test
    fun jpegIsAccepted() {
        assertEquals(ImageValidationResult.Ok, image("image/jpeg").validate())
    }

    @Test
    fun pngIsAccepted() {
        assertEquals(ImageValidationResult.Ok, image("image/png").validate())
    }

    @Test
    fun gifIsAccepted() {
        assertEquals(ImageValidationResult.Ok, image("image/gif").validate())
    }

    @Test
    fun webpIsAccepted() {
        assertEquals(ImageValidationResult.Ok, image("image/webp").validate())
    }

    @Test
    fun allAllowedTypesAreAccepted() {
        for (mimeType in ALLOWED_IMAGE_MIME_TYPES) {
            assertEquals(ImageValidationResult.Ok, image(mimeType).validate(), "Expected Ok for $mimeType")
        }
    }

    @Test
    fun pdfIsRejectedAsUnsupportedType() {
        val result = image("application/pdf").validate()
        val unsupported = assertIs<ImageValidationResult.UnsupportedType>(result)
        assertEquals("application/pdf", unsupported.mimeType)
    }

    @Test
    fun bmpIsRejectedAsUnsupportedType() {
        val result = image("image/bmp").validate()
        val unsupported = assertIs<ImageValidationResult.UnsupportedType>(result)
        assertEquals("image/bmp", unsupported.mimeType)
    }

    @Test
    fun emptyMimeTypeIsRejected() {
        val result = image("").validate()
        assertIs<ImageValidationResult.UnsupportedType>(result)
    }

    @Test
    fun exactMaxSizeIsAccepted() {
        assertEquals(ImageValidationResult.Ok, image("image/png", MAX_IMAGE_SIZE_BYTES).validate())
    }

    @Test
    fun oneByteOverMaxSizeIsRejected() {
        val result = image("image/png", MAX_IMAGE_SIZE_BYTES + 1).validate()
        assertEquals(ImageValidationResult.TooLarge, result)
    }

    @Test
    fun tooLargeWithValidMimeTypeIsRejectedAsTooLarge() {
        val result = image("image/jpeg", MAX_IMAGE_SIZE_BYTES + 100).validate()
        assertEquals(ImageValidationResult.TooLarge, result)
    }

    @Test
    fun unsupportedTypeCheckedBeforeSize() {
        val result = image("image/bmp", MAX_IMAGE_SIZE_BYTES + 1).validate()
        assertIs<ImageValidationResult.UnsupportedType>(result)
    }

    @Test
    fun zeroSizeIsAccepted() {
        assertEquals(ImageValidationResult.Ok, image("image/jpeg", 0L).validate())
    }
}
