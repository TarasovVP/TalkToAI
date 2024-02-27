package com.vnstudio.talktoai.data.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiErrorResponse(
    val error: ApiError?
) : Parcelable