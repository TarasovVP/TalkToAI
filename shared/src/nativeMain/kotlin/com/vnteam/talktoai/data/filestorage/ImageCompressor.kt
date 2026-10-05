package com.vnteam.talktoai.data.filestorage

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.create
import platform.UIKit.UIColor
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIRectFill

@OptIn(ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ImageCompressor {

    actual suspend fun makeThumbnail(base64Data: String, mimeType: String): String? = runCatching {
        val data = NSData.create(base64EncodedString = base64Data, options = 0uL) ?: return@runCatching null
        val image = UIImage(data = data)
        val (width, height) = image.size.useContents { width to height }
        val scale = THUMBNAIL_MAX_DIMENSION_PX / maxOf(width, height)
        val targetSize = if (scale < 1.0) CGSizeMake(width * scale, height * scale) else image.size
        val (targetWidth, targetHeight) = targetSize.useContents { width to height }
        UIGraphicsBeginImageContextWithOptions(targetSize, true, 1.0)
        UIColor.whiteColor.setFill()
        UIRectFill(CGRectMake(0.0, 0.0, targetWidth, targetHeight))
        image.drawInRect(CGRectMake(0.0, 0.0, targetWidth, targetHeight))
        val resized = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        val jpeg = resized?.let { UIImageJPEGRepresentation(it, THUMBNAIL_JPEG_QUALITY / 100.0) }
            ?: return@runCatching null
        jpeg.base64EncodedStringWithOptions(0uL)
    }.getOrNull()
}
