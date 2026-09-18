package com.vnteam.talktoai.data.filepicker

import com.vnteam.talktoai.domain.models.PickedImage

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class FilePicker {
    suspend fun pickImage(): PickedImage?
}
