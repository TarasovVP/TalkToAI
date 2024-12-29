package com.vnteam.talktoai.presentation.uimodels.screen

import androidx.compose.runtime.mutableStateOf
import com.vnteam.talktoai.CommonExtensions.isNotNull
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.NavigationScreen.Companion.isSettingsScreen

data class ScreenState(
    var appMessageState: AppMessageState? = AppMessageState(),
    var isDarkTheme: Boolean? = false,
    var language: String? = null,
    var isLoggedInUser: Boolean? = false,
    var isOnboardingSeen: Boolean? = false,
    var isProgressVisible: Boolean = false,
    var isScreenUpdatingNeeded: Boolean = false,
    var currentChat: Chat? = null,
    var currentScreenRoute: String? = null
) {
    val isMessageActionModeState = mutableStateOf<Boolean?>(null)

    val isReadyToLaunch: Boolean
        get() = isDarkTheme.isNotNull() && language.isNotNull() && isLoggedInUser.isNotNull() && isOnboardingSeen.isNotNull()

    val isChatScreen: Boolean
        get() = currentScreenRoute == NavigationScreen.ChatScreen.route

    val isSettingsScreen: Boolean
        get() = isSettingsScreen(currentScreenRoute)

    val isSecondaryScreen: Boolean
        get() = currentScreenRoute == NavigationScreen.SettingsSignUpScreen.route

    val startDestination: String
        get() = when {
            isOnboardingSeen == false -> NavigationScreen.ONBOARDING_SCREEN
            isLoggedInUser == false -> NavigationScreen.LOGIN_SCREEN
            else -> NavigationScreen.CHAT_SCREEN
        }
}