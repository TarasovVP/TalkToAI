package com.vnstudio.talktoai.domain.models

import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.infrastructure.Constants.SUCCESS_MESSAGE
import kotlinx.serialization.Serializable

@Serializable
data class InfoMessage(
    var message: String = String.EMPTY,
    var type: String = SUCCESS_MESSAGE,
)