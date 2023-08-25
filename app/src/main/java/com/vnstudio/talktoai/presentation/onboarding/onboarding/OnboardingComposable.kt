package com.vnstudio.talktoai.presentation.onboarding.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.presentation.components.PrimaryButton
import com.vnstudio.talktoai.ui.theme.Neutral50
import com.vnstudio.talktoai.ui.theme.Primary500

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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Primary500, shape = RoundedCornerShape(16.dp))
        ) {
            Text(text = "Привет, Я - Искусственный Интеллект. Page $page", textAlign = TextAlign.Center, color = Neutral50, modifier = Modifier
                .fillMaxWidth().padding(16.dp))
        }
        Image(painter = painterResource(id = R.drawable.onboarding_intro), contentDescription = "Onboarding icon", modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp))
        Image(painter = painterResource(id = when(page) {
            1 -> R.drawable.ic_tab_two
            2 -> R.drawable.ic_tab_three
            3 -> R.drawable.ic_tab_four
            else -> R.drawable.ic_tab_one
        }), contentDescription = "Onboarding tab $page", modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp))
        PrimaryButton(text = if (page == 3) "Войти" else "Дальше", modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp), onClick = onClick)
    }
}