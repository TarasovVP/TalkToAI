package com.vn.talktoai

import com.google.gson.annotations.SerializedName

data class ApiResponse(val id: String?,
                       @SerializedName("object") val chatObject: String?,
                       val created: String?,
                       val model: String?,
                       val usage: Usage?,
                       val choices: List<Choice>?)