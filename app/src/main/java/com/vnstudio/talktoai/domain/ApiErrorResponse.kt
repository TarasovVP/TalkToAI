package com.vnstudio.talktoai.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiErrorResponse(
    val error: ApiError?
) : Parcelable