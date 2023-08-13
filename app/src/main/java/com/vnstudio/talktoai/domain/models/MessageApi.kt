package com.vnstudio.talktoai.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageApi(val role: String?,
                      val content: String?): Parcelable