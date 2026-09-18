package com.vnteam.talktoai.data.filepicker

import com.vnteam.talktoai.domain.models.PickedImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Base64
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FilePicker {

    actual suspend fun pickImage(): PickedImage? = withContext(Dispatchers.IO) {
        val chooser = JFileChooser().apply {
            dialogTitle = "Select Image"
            fileSelectionMode = JFileChooser.FILES_ONLY
            fileFilter = FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif", "webp")
        }
        val result = withContext(Dispatchers.Main) {
            chooser.showOpenDialog(null)
        }
        if (result != JFileChooser.APPROVE_OPTION) return@withContext null
        val file = chooser.selectedFile ?: return@withContext null
        val bytes = file.readBytes()
        val mimeType = detectMimeType(bytes)
        PickedImage(
            base64Data = Base64.getEncoder().encodeToString(bytes),
            mimeType = mimeType,
            sizeBytes = bytes.size.toLong(),
        )
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
