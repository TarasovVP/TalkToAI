package com.vnteam.talktoai.domain.models

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants.SUCCESS_MESSAGE
import kotlinx.serialization.Serializable

@Serializable
data class InfoMessage(
    var message: String = String.EMPTY,
    var type: String = SUCCESS_MESSAGE,
)