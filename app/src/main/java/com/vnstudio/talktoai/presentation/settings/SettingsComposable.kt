package com.vnstudio.talktoai.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.vnstudio.talktoai.presentation.components.PrimaryButton

@Composable
fun SettingsScreen(onClick: () -> Unit, ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Settings Screen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = onClick)
    }
}