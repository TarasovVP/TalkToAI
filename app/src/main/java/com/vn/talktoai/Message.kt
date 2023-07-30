package com.vn.talktoai

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(val role: String?,
                   val content: String?): Parcelable