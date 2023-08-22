package com.vnstudio.talktoai.presentation.onboarding.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.vnstudio.talktoai.presentation.base.PrimaryButton

@Composable
fun SignUpScreen(onClick: () -> Unit, ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SignUp Screen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = onClick)
    }
}