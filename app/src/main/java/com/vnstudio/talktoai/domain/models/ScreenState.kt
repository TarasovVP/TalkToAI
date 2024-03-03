package com.vnstudio.talktoai.domain.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ScreenState(
    val progressVisibilityState: MutableState<Boolean> = mutableStateOf(false),
    val infoMessageState: MutableState<InfoMessage?> = mutableStateOf(null),
    val nextScreenState: MutableState<String?> = mutableStateOf(null),
)
