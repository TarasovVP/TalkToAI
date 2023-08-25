package com.vnstudio.talktoai.presentation.settings.settings_list

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.presentation.components.PrimaryButton

@Composable
fun SettingsListScreen(onClick: () -> Unit, ) {

    val viewModel: SettingsListViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SettingsListScreen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = onClick)
    }
}