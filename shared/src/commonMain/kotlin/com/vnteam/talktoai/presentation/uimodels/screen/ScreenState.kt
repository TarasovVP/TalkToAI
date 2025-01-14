package com.vnteam.talktoai.presentation.uimodels.screen

import androidx.compose.runtime.mutableStateOf
import com.vnteam.talktoai.CommonExtensions.isNotNull
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.ui.NavigationScreen

data class ScreenState(
    var appMessage: AppMessage? = null,
    var isDarkTheme: Boolean? = false,
    var language: String? = null,
    var userLogin: String? = null,
    var isOnboardingSeen: Boolean? = false,
    var isProgressVisible: Boolean = false,
    var isScreenUpdatingNeeded: Boolean = false,
    var currentChat: Chat? = null,
    var currentScreenRoute: String? = null
) {
    val isMessageActionModeState = mutableStateOf<Boolean?>(null)

    val isReadyToLaunch: Boolean
        get() = isDarkTheme.isNotNull() && language.isNotNull() && userLogin.isNotNull() && isOnboardingSeen.isNotNull()

    val isChatScreen: Boolean
        get() = currentScreenRoute?.contains(NavigationScreen.CHAT_DESTINATION).isTrue()

    val isSettingsScreen: Boolean
        get() = NavigationScreen.isSettingsScreen(currentScreenRoute)

    val isSecondaryScreen: Boolean
        get() = currentScreenRoute == NavigationScreen.SettingsSignUpScreen.route

    val startDestination: String
        get() = when {
            isOnboardingSeen == false -> NavigationScreen.ONBOARDING_SCREEN
            userLogin.isNullOrEmpty() -> NavigationScreen.LOGIN_SCREEN
            else -> NavigationScreen.CHAT_SCREEN
        }
}