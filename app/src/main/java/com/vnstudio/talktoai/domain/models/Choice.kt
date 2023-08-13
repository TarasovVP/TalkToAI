package com.vnstudio.talktoai.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Choice(val message: MessageApi?,
                  @SerializedName("finish_reason") val finishReason: String?,
                  val index: Int?): Parcelable