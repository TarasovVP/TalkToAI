package com.vnteam.talktoai.domain.models

val ALLOWED_IMAGE_MIME_TYPES = setOf("image/jpeg", "image/png", "image/gif", "image/webp")
const val MAX_IMAGE_SIZE_BYTES = 5L * 1024L * 1024L

sealed class ImageValidationResult {
    object Ok : ImageValidationResult()
    object TooLarge : ImageValidationResult()
    data class UnsupportedType(val mimeType: String) : ImageValidationResult()
}

data class PickedImage(
    val base64Data: String,
    val mimeType: String,
    val sizeBytes: Long,
)

fun PickedImage.validate(): ImageValidationResult = when {
    mimeType !in ALLOWED_IMAGE_MIME_TYPES -> ImageValidationResult.UnsupportedType(mimeType)
    sizeBytes > MAX_IMAGE_SIZE_BYTES -> ImageValidationResult.TooLarge
    else -> ImageValidationResult.Ok
}
