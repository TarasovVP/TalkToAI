package com.vnteam.talktoai.data.filepicker

import com.vnteam.talktoai.domain.models.PickedImage
import kotlinx.browser.document
import kotlinx.coroutines.CompletableDeferred
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import org.w3c.files.get

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FilePicker {

    actual suspend fun pickImage(): PickedImage? {
        val deferred = CompletableDeferred<PickedImage?>()

        val input = (document.createElement("input") as HTMLInputElement).apply {
            type = "file"
            accept = "image/*"
        }

        input.onchange = { _: Event ->
            val file = input.files?.get(0)
            if (file == null) {
                deferred.complete(null)
            } else {
                val mimeType = file.type.ifBlank { "application/octet-stream" }
                val reader = FileReader()
                reader.onload = { _: Event ->
                    val dataUrl = reader.result as? String
                    val base64 = dataUrl?.substringAfter(",")
                    val sizeBytes = file.size.toLong()
                    deferred.complete(
                        if (base64 != null) PickedImage(base64Data = base64, mimeType = mimeType, sizeBytes = sizeBytes)
                        else null
                    )
                }
                reader.onerror = { _: Event -> deferred.complete(null) }
                reader.readAsDataURL(file)
            }
        }

        input.click()
        return deferred.await()
    }
}
