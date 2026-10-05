package com.vnteam.talktoai.data.filestorage

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ImageCompressor {
    suspend fun makeThumbnail(base64Data: String, mimeType: String): String?
}

internal const val THUMBNAIL_MAX_DIMENSION_PX = 300
internal const val THUMBNAIL_JPEG_QUALITY = 60
