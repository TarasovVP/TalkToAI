package com.vnstudio.talktoai.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usage(
    @SerializedName("prompt_tokens") val promptTokens: String?,
    @SerializedName("completion_tokens") val completionTokens: String?,
    @SerializedName("total_tokens") val totalTokens: String?,
) : Parcelable