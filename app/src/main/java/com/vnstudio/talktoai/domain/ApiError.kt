package com.vnstudio.talktoai.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiError(
    val message: String?,
    val type: String?,
    val param: String?,
    val code: String?
) : Parcelable