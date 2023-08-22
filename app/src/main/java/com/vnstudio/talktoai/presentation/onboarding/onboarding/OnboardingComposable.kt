package com.vnstudio.talktoai.presentation.onboarding.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.presentation.base.PrimaryButton

@Composable
fun OnboardingScreen(onClick: () -> Unit, ) {
    val viewModel: OnBoardingViewModel = hiltViewModel()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Onboarding Screen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = {
            viewModel.setOnBoardingSeen()
            onClick.invoke()
        })
    }
}