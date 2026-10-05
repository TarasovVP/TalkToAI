package com.vnteam.talktoai.data.filestorage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ImageCompressor {

    actual suspend fun makeThumbnail(base64Data: String, mimeType: String): String? =
        withContext(Dispatchers.IO) {
            runCatching {
                val bytes = Base64.decode(base64Data)
                val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
                var sample = 1
                while (bounds.outWidth / sample > THUMBNAIL_MAX_DIMENSION_PX * 2 ||
                    bounds.outHeight / sample > THUMBNAIL_MAX_DIMENSION_PX * 2
                ) sample *= 2
                val decoded = BitmapFactory.decodeByteArray(
                    bytes, 0, bytes.size,
                    BitmapFactory.Options().apply { inSampleSize = sample }
                ) ?: return@runCatching null
                val scale = THUMBNAIL_MAX_DIMENSION_PX.toFloat() / maxOf(decoded.width, decoded.height)
                val scaled = if (scale < 1f) {
                    Bitmap.createScaledBitmap(
                        decoded, (decoded.width * scale).toInt(), (decoded.height * scale).toInt(), true
                    )
                } else decoded
                val flattened = Bitmap.createBitmap(scaled.width, scaled.height, Bitmap.Config.ARGB_8888)
                Canvas(flattened).apply {
                    drawColor(Color.WHITE)
                    drawBitmap(scaled, 0f, 0f, null)
                }
                val out = ByteArrayOutputStream()
                flattened.compress(Bitmap.CompressFormat.JPEG, THUMBNAIL_JPEG_QUALITY, out)
                Base64.encode(out.toByteArray())
            }.getOrNull()
        }
}
