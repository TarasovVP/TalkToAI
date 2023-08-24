package com.vnstudio.talktoai.presentation.onboarding.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.presentation.components.PrimaryButton

@Composable
fun OnboardingScreen(onNextScreen: () -> Unit, ) {
    val viewModel: OnBoardingViewModel = hiltViewModel()
    val pageState = remember {
        mutableStateOf(0)
    }
    val onBoardingSeenState = viewModel.onBoardingSeenLiveData.observeAsState()
    LaunchedEffect(onBoardingSeenState.value) {
        onBoardingSeenState.value?.let {
            onNextScreen.invoke()
        }
    }
    OnboardingPage(pageState.value) {
        if (pageState.value == 3) {
            viewModel.setOnBoardingSeen()
        } else {
            pageState.value++
        }
    }
}

@Composable
fun OnboardingPage(page: Int, onClick: () -> Unit, ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Page $page")
        PrimaryButton(text = if (page == 3) "Enter" else "Next", modifier = Modifier, onClick = onClick)
    }
}