package com.vnteam.talktoai.data.filestorage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ImageCompressor {

    actual suspend fun makeThumbnail(base64Data: String, mimeType: String): String? =
        withContext(Dispatchers.IO) {
            runCatching {
                val bytes = Base64.decode(base64Data)
                val original = ImageIO.read(ByteArrayInputStream(bytes)) ?: return@runCatching null
                val scale = THUMBNAIL_MAX_DIMENSION_PX.toDouble() / maxOf(original.width, original.height)
                val width = if (scale < 1.0) (original.width * scale).toInt() else original.width
                val height = if (scale < 1.0) (original.height * scale).toInt() else original.height
                val scaledInstance = original.getScaledInstance(width, height, Image.SCALE_SMOOTH)
                val flattened = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
                flattened.createGraphics().apply {
                    color = Color.WHITE
                    fillRect(0, 0, width, height)
                    drawImage(scaledInstance, 0, 0, null)
                    dispose()
                }
                val out = ByteArrayOutputStream()
                val writer = ImageIO.getImageWritersByFormatName("jpg").next()
                val param = writer.defaultWriteParam.apply {
                    compressionMode = ImageWriteParam.MODE_EXPLICIT
                    compressionQuality = THUMBNAIL_JPEG_QUALITY / 100f
                }
                writer.output = ImageIO.createImageOutputStream(out)
                writer.write(null, IIOImage(flattened, null, null), param)
                writer.dispose()
                Base64.encode(out.toByteArray())
            }.getOrNull()
        }
}
