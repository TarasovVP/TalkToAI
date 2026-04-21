package com.vnteam.talktoai.presentation.screens.settings.settings_list

import androidx.compose.runtime.Composable
import com.vnteam.talktoai.presentation.LocalScreenState

@Composable
fun SettingsListScreen() {
    val screenState = LocalScreenState.current
    SettingsListComposable(
        currentRouteState = screenState.value.currentScreenRoute
    ) { route ->
        screenState.value = screenState.value.copy(currentScreenRoute = route)
    }
}
