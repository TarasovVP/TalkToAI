package com.vnstudio.talktoai.domain.models

import android.os.Parcelable
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feedback(
    var user: String = String.EMPTY,
    var message: String = String.EMPTY,
    var time: Long = 0,
) : Parcelable