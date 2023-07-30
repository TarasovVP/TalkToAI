package com.vn.talktoai

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Choice(val message: Message?,
                  @SerializedName("finish_reason") val finishReason: String?,
                  val index: Int?): Parcelable