package com.vnstudio.talktoai.presentation.settings.settings_theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.presentation.components.PrimaryButton

@Composable
fun SettingsThemeScreen(onClick: () -> Unit, ) {

    val viewModel: SettingsThemeViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SettingsThemeScreen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = onClick)
    }
}