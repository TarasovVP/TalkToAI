package com.vn.talktoai

import com.google.gson.annotations.SerializedName

data class Usage(@SerializedName("prompt_tokens") val promptTokens: String?,
                 @SerializedName("completion_tokens") val completionTokens: String?,
                 @SerializedName("total_tokens") val totalTokens: String?)