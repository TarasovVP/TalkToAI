package com.vnteam.talktoai.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.presentation.screens.main.AppContent
import com.vnteam.talktoai.presentation.ui.components.SplashScreen
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.resources.getStringResourcesByLocale
import com.vnteam.talktoai.presentation.ui.theme.AppTheme
import com.vnteam.talktoai.presentation.uimodels.screen.AppMessage
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.settings.AppViewModel
import secrets.Secrets

@Composable
fun App(appViewModel: AppViewModel) {
    val screenState = appViewModel.screenState.collectAsState()
    Secrets
    val isSplashScreenVisible = remember { mutableStateOf(true) }
    val localScreenState = remember { mutableStateOf(screenState.value) }

    LaunchedEffect(
        screenState.value.idToken,
        screenState.value.isDarkTheme,
        screenState.value.language,
        screenState.value.userEmail,
        screenState.value.isOnboardingSeen,
    ) {
        localScreenState.value = localScreenState.value.copy(
            idToken = screenState.value.idToken,
            isDarkTheme = screenState.value.isDarkTheme,
            language = screenState.value.language,
            userEmail = screenState.value.userEmail,
            isOnboardingSeen = screenState.value.isOnboardingSeen,
        )
    }

    if (localScreenState.value.isReadyToLaunch && isSplashScreenVisible.value.not()) {
        CompositionLocalProvider(
            LocalStringResources provides getStringResourcesByLocale(
                localScreenState.value.language.orEmpty()
            ), LocalScreenState provides localScreenState
        ) {
            AppTheme(localScreenState.value.isDarkTheme.isTrue()) {
                AppContent(appViewModel)
            }
        }
    } else {
        SplashScreen {
            isSplashScreenVisible.value = false
        }
    }
}

val LocalScreenState = compositionLocalOf<MutableState<ScreenState>> {
    error("No ScreenState provided")
}

@Composable
fun updateScreenState(
    isProgressVisible: Boolean = false,
    appMessage: AppMessage? = null,
    screenRoute: String? = null,
) {
    val localScreenState = LocalScreenState.current
    localScreenState.value = localScreenState.value.copy(
        isProgressVisible = isProgressVisible,
        appMessage = appMessage ?: localScreenState.value.appMessage,
        currentScreenRoute = screenRoute ?: localScreenState.value.currentScreenRoute
    )
}

