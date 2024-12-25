package com.vnteam.talktoai.presentation.uimodels.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.vnteam.talktoai.CommonExtensions.isNotNull
import com.vnteam.talktoai.domain.models.InfoMessage

data class ScreenState(
    var appBarState: AppBarState? = AppBarState(),
    var floatingActionState: FloatingActionState? = FloatingActionState(),
    var appMessageState: AppMessageState? = AppMessageState(),
    var isDarkTheme: Boolean? = false,
    var language: String? = null,
    var loggedInUser: Boolean? = false,
    var isOnboardingSeen: Boolean? = false,
    var isProgressVisible: Boolean = false,
    var isScreenUpdatingNeeded: Boolean = false
) {
    val isReadyToLaunch: Boolean
        get() = isDarkTheme.isNotNull() && language.isNotNull() && loggedInUser.isNotNull() && isOnboardingSeen.isNotNull()

    val currentScreenState: MutableState<String?> = mutableStateOf(null)
    val infoMessageState: MutableState<InfoMessage?> = mutableStateOf(null)
}