package com.vnteam.talktoai.data.filepicker

import com.vnteam.talktoai.domain.models.PickedImage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CompletableDeferred
import platform.Foundation.NSData
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import platform.posix.memcpy
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FilePicker {

    // PHPickerViewController keeps its delegate weakly, so retain it until the
    // picker reports a selection or cancellation.
    private var activeDelegate: PHPickerViewControllerDelegateProtocol? = null

    @OptIn(ExperimentalEncodingApi::class, ExperimentalForeignApi::class)
    actual suspend fun pickImage(): PickedImage? {
        val deferred = CompletableDeferred<PickedImage?>()

        val config = PHPickerConfiguration()
        config.filter = PHPickerFilter.imagesFilter
        config.selectionLimit = 1

        val picker = PHPickerViewController(configuration = config)

        val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
            override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                picker.dismissViewControllerAnimated(true, null)
                activeDelegate = null
                val result = didFinishPicking.firstOrNull() as? PHPickerResult
                if (result == null) {
                    deferred.complete(null)
                    return
                }
                result.itemProvider.loadDataRepresentationForTypeIdentifier("public.image") { data, _ ->
                    val nsData = data as? NSData
                    if (nsData == null) {
                        deferred.complete(null)
                        return@loadDataRepresentationForTypeIdentifier
                    }
                    val bytes = ByteArray(nsData.length.toInt())
                    bytes.usePinned { pinned ->
                        memcpy(pinned.addressOf(0), nsData.bytes, nsData.length)
                    }
                    val mimeType = detectMimeType(bytes)
                    deferred.complete(
                        PickedImage(
                            base64Data = Base64.encode(bytes),
                            mimeType = mimeType,
                            sizeBytes = bytes.size.toLong(),
                        )
                    )
                }
            }
        }

        activeDelegate = delegate
        picker.delegate = delegate
        UIApplication.sharedApplication.keyWindow?.rootViewController
            ?.presentViewController(picker, animated = true, completion = null)

        return deferred.await()
    }
}

private fun detectMimeType(bytes: ByteArray): String {
    if (bytes.size < 4) return "application/octet-stream"
    return when {
        bytes[0] == 0xFF.toByte() && bytes[1] == 0xD8.toByte() -> "image/jpeg"
        bytes[0] == 0x89.toByte() && bytes[1] == 0x50.toByte() && bytes[2] == 0x4E.toByte() && bytes[3] == 0x47.toByte() -> "image/png"
        bytes[0] == 0x47.toByte() && bytes[1] == 0x49.toByte() && bytes[2] == 0x46.toByte() -> "image/gif"
        bytes[0] == 0x52.toByte() && bytes[1] == 0x49.toByte() && bytes[2] == 0x46.toByte() && bytes[3] == 0x46.toByte() -> "image/webp"
        else -> "application/octet-stream"
    }
}
