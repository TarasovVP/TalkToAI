package com.vnstudio.talktoai.presentation.settings.settings_privacy_policy

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.presentation.components.PrimaryButton

@Composable
fun SettingsPrivacyPolicyScreen(onClick: () -> Unit, ) {

    val viewModel: SettingsPrivacyPolicyViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SettingsPrivacyPolicyScreen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = onClick)
    }
}