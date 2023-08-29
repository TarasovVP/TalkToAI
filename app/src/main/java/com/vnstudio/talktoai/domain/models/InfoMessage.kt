package com.vnstudio.talktoai.domain.models

import android.os.Parcelable
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.infrastructure.Constants.SUCCESS_MESSAGE
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoMessage(
    var message: String = String.EMPTY,
    var type: String = SUCCESS_MESSAGE
) : Parcelable