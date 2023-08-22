package com.vnstudio.talktoai.presentation.onboarding.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.vnstudio.talktoai.presentation.base.PrimaryButton

@Composable
fun LoginScreen(onClick: () -> Unit, ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login Screen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = onClick)
    }
}