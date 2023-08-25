package com.vnstudio.talktoai.presentation.settings.settings_sign_up

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.presentation.components.PrimaryButton

@Composable
fun SettingsSignUpScreen(onClick: () -> Unit, ) {

    val viewModel: SettingSignUpViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SettingsSignUpScreen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = onClick)
    }
}