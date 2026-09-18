package com.vnteam.talktoai.data.filepicker

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import com.vnteam.talktoai.domain.models.PickedImage
import kotlinx.coroutines.CompletableDeferred
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FilePicker {

    private var activityProvider: () -> ComponentActivity = {
        error("Call setActivityProvider before using FilePicker")
    }
    private var launcher: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private var pending: CompletableDeferred<Uri?>? = null

    fun setActivityProvider(provider: () -> ComponentActivity) {
        activityProvider = provider
    }

    fun registerLauncher(activity: ComponentActivity) {
        launcher = activity.registerForActivityResult(PickVisualMedia()) { uri ->
            pending?.complete(uri)
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    actual suspend fun pickImage(): PickedImage? {
        val deferred = CompletableDeferred<Uri?>()
        pending = deferred
        launcher?.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            ?: return null
        val uri = deferred.await() ?: return null
        val activity = activityProvider()
        val resolver = activity.contentResolver
        val mimeType = resolver.getType(uri) ?: return null
        val bytes = resolver.openInputStream(uri)?.use { it.readBytes() } ?: return null
        return PickedImage(
            base64Data = Base64.encode(bytes),
            mimeType = mimeType,
            sizeBytes = bytes.size.toLong(),
        )
    }
}
