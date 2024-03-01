package com.vnstudio.talktoai.presentation.screens.authorization.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

import com.vnstudio.talktoai.presentation.components.PrimaryButton
import com.vnstudio.talktoai.presentation.components.painterRes
import com.vnstudio.talktoai.presentation.components.stringRes
import com.vnstudio.talktoai.presentation.theme.Neutral50
import com.vnstudio.talktoai.presentation.theme.Primary500

@Composable
fun OnboardingScreen(onNextScreen: () -> Unit) {
    val viewModel: OnBoardingViewModel = koinViewModel()
    val pageState = remember {
        mutableIntStateOf(0)
    }
    val onBoardingSeenState = viewModel.onBoardingSeenLiveData.collectAsState()
    LaunchedEffect(onBoardingSeenState.value) {
        if (onBoardingSeenState.value) {
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
fun OnboardingPage(page: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .background(color = Primary500, shape = RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "Привет, Я - Искусственный Интеллект. Page $page",
                textAlign = TextAlign.Center,
                color = Neutral50,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        Image(
            painter = painterRes("onboarding_intro"),
            contentDescription = "Onboarding icon",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        )
        Image(
            painter = painterRes(
                when (page) {
                    1 -> "ic_tab_two"
                    2 -> "ic_tab_three"
                    3 -> "ic_tab_four"
                    else -> "ic_tab_one"
                }
            ), contentDescription = "Onboarding tab $page", modifier = Modifier
                .fillMaxWidth()
        )
        PrimaryButton(
            text = if (page == 3) stringRes().AUTHORIZATION_ENTER else stringRes().BUTTON_NEXT, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp), onClick = onClick
        )
    }
}