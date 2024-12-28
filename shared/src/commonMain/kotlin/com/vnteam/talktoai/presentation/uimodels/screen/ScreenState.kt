package com.vnteam.talktoai.presentation.uimodels.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.vnteam.talktoai.CommonExtensions.isNotNull
import com.vnteam.talktoai.CommonExtensions.isNotTrue
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.InfoMessage
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.NavigationScreen.Companion.isSettingsScreen

data class ScreenState(
    var appBarState: AppBarState? = AppBarState(),
    var floatingActionState: FloatingActionState? = FloatingActionState(),
    var appMessageState: AppMessageState? = AppMessageState(),
    var isDarkTheme: Boolean? = false,
    var language: String? = null,
    var loggedInUser: Boolean? = false,
    var isOnboardingSeen: Boolean? = false,
    var isProgressVisible: Boolean = false,
    var isScreenUpdatingNeeded: Boolean = false,
    var currentChat: Chat? = null,
    var isMessageActionModeState: Boolean = false,
    var currentScreenRoute: String? = null
) {
    val infoMessageState: MutableState<InfoMessage?> = mutableStateOf(null)

    val isReadyToLaunch: Boolean
        get() = isDarkTheme.isNotNull() && language.isNotNull() && loggedInUser.isNotNull() && isOnboardingSeen.isNotNull()

    val isChatScreen: Boolean
        get() = currentScreenRoute == NavigationScreen.ChatScreen().route

    val isSettingsScreen: Boolean
        get() = isSettingsScreen(currentScreenRoute)

    val isDrawerGesturesEnabled: Boolean
        get() = isSettingsScreen(currentScreenRoute) || (isChatScreen && isMessageActionModeState.isNotTrue())
}