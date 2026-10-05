package com.vnteam.talktoai.data.filestorage

import kotlinx.browser.document
import kotlinx.coroutines.suspendCancellableCoroutine
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import kotlin.coroutines.resume

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ImageCompressor {

    actual suspend fun makeThumbnail(base64Data: String, mimeType: String): String? =
        suspendCancellableCoroutine { cont ->
            val img = document.createElement("img") as HTMLImageElement
            img.onload = {
                val scale = THUMBNAIL_MAX_DIMENSION_PX.toDouble() / maxOf(img.width, img.height)
                val width = if (scale < 1.0) (img.width * scale).toInt() else img.width
                val height = if (scale < 1.0) (img.height * scale).toInt() else img.height
                val canvas = document.createElement("canvas") as HTMLCanvasElement
                canvas.width = width
                canvas.height = height
                val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
                ctx.fillStyle = "white"
                ctx.fillRect(0.0, 0.0, width.toDouble(), height.toDouble())
                ctx.drawImage(img, 0.0, 0.0, width.toDouble(), height.toDouble())
                val dataUrl = canvas.toDataURL("image/jpeg", THUMBNAIL_JPEG_QUALITY / 100.0)
                cont.resume(dataUrl.substringAfter("base64,", missingDelimiterValue = "").ifEmpty { null })
                Unit
            }
            img.onerror = { _, _, _, _, _ -> cont.resume(null); Unit }
            img.src = "data:$mimeType;base64,$base64Data"
        }
}
