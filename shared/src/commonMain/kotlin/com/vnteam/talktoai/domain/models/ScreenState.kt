package com.vnteam.talktoai.domain.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.vnteam.talktoai.domain.models.InfoMessage

data class ScreenState(
    val progressVisibilityState: MutableState<Boolean> = mutableStateOf(false),
    val infoMessageState: MutableState<InfoMessage?> = mutableStateOf(null),
    val currentScreenState: MutableState<String?> = mutableStateOf(null),
)
