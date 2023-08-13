package com.vnstudio.talktoai.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.vnstudio.talktoai.domain.models.Choice
import com.vnstudio.talktoai.domain.models.Usage
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiResponse(val id: String?,
                       @SerializedName("object") val chatObject: String?,
                       val created: String?,
                       val model: String?,
                       val usage: Usage?,
                       val choices: List<Choice>?): Parcelable