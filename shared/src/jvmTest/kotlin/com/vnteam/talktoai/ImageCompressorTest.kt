package com.vnteam.talktoai

import com.vnteam.talktoai.data.filestorage.ImageCompressor
import com.vnteam.talktoai.data.filestorage.THUMBNAIL_MAX_DIMENSION_PX
import kotlinx.coroutines.test.runTest
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalEncodingApi::class)
class ImageCompressorTest {

    private val compressor = ImageCompressor()

    private fun solidColorImageBase64(width: Int, height: Int, format: String, color: Color): String {
        val type = if (format == "jpg") BufferedImage.TYPE_INT_RGB else BufferedImage.TYPE_INT_ARGB
        val image = BufferedImage(width, height, type)
        image.createGraphics().apply {
            this.color = color
            fillRect(0, 0, width, height)
            dispose()
        }
        val out = ByteArrayOutputStream()
        ImageIO.write(image, format, out)
        return Base64.encode(out.toByteArray())
    }

    @Test
    fun makeThumbnailReturnsSmallerNonEmptyJpeg() = runTest {
        val original = solidColorImageBase64(1200, 900, "jpg", Color.BLUE)
        val thumbnail = compressor.makeThumbnail(original, "image/jpeg")
        assertNotNull(thumbnail)
        assertTrue(thumbnail.isNotEmpty())
        assertTrue(Base64.decode(thumbnail).size < Base64.decode(original).size)
    }

    @Test
    fun makeThumbnailRespectsMaxDimension() = runTest {
        val original = solidColorImageBase64(1200, 900, "jpg", Color.BLUE)
        val thumbnail = compressor.makeThumbnail(original, "image/jpeg")!!
        val decoded = ImageIO.read(ByteArrayInputStream(Base64.decode(thumbnail)))
        assertTrue(maxOf(decoded.width, decoded.height) <= THUMBNAIL_MAX_DIMENSION_PX)
    }

    @Test
    fun makeThumbnailOnTransparentPngFillsWhiteNotBlack() = runTest {
        val width = 50
        val height = 50
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val out = ByteArrayOutputStream()
        ImageIO.write(image, "png", out)
        val original = Base64.encode(out.toByteArray())

        val thumbnail = compressor.makeThumbnail(original, "image/png")!!
        val decoded = ImageIO.read(ByteArrayInputStream(Base64.decode(thumbnail)))
        val cornerPixel = decoded.getRGB(0, 0)
        val r = (cornerPixel shr 16) and 0xFF
        val g = (cornerPixel shr 8) and 0xFF
        val b = cornerPixel and 0xFF
        assertTrue(r > 200 && g > 200 && b > 200) // white-ish, not black
    }

    @Test
    fun makeThumbnailReturnsNullForGarbageInput() = runTest {
        val thumbnail = compressor.makeThumbnail("not valid base64 image data at all", "image/jpeg")
        assertNull(thumbnail)
    }
}
