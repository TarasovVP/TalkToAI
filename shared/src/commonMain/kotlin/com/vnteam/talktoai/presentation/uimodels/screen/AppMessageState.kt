package com.vnteam.talktoai.presentation.uimodels.screen

data class AppMessageState(
    var messageVisible: Boolean = false,
    var isMessageError: Boolean = false,
    var messageText: String = ""
)
