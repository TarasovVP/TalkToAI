package com.vnstudio.talktoai.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.vnstudio.talktoai.domain.models.Choice
import com.vnstudio.talktoai.domain.models.Usage
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiErrorResponse(
    val error: ApiError?
) : Parcelable