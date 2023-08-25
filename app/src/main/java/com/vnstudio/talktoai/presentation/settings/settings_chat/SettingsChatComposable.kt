package com.vnstudio.talktoai.presentation.settings.settings_chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.presentation.components.PrimaryButton

@Composable
fun SettingsChatScreen(onClick: () -> Unit, ) {

    val viewModel: SettingsChatViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SettingsChatScreen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = onClick)
    }
}