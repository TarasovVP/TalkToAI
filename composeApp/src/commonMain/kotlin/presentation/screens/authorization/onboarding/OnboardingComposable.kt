package presentation.screens.authorization.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.ic_tab_four
import com.vnteam.talktoai.ic_tab_one
import com.vnteam.talktoai.ic_tab_three
import com.vnteam.talktoai.ic_tab_two
import com.vnteam.talktoai.onboarding_intro
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral50
import com.vnteam.talktoai.presentation.ui.theme.Primary500
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.OnBoardingViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingContent(screenState: ScreenState) {

    val viewModel: OnBoardingViewModel = koinViewModel()
    val pageState = remember {
        mutableIntStateOf(0)
    }
    val onBoardingSeenState = viewModel.onBoardingSeenLiveData.collectAsState()
    LaunchedEffect(onBoardingSeenState.value) {
        if (onBoardingSeenState.value) {
            screenState.currentScreenRoute = NavigationScreen.LoginScreen().route
            viewModel.onBoardingSeenLiveData.value = false
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
                text = when (page) {
                    1 -> LocalStringResources.current.ONBOARDING_INTRO
                    2 -> LocalStringResources.current.ONBOARDING_FILTER_CONDITIONS
                    3 -> LocalStringResources.current.ONBOARDING_INFO
                    else -> LocalStringResources.current.ONBOARDING_PERMISSIONS
                },
                textAlign = TextAlign.Center,
                color = Neutral50,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        Image(
            painter = painterResource(Res.drawable.onboarding_intro),
            contentDescription = LocalStringResources.current.ONBOARDING_ICON,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        )
        Image(
            painter = painterResource(
                when (page) {
                    1 -> Res.drawable.ic_tab_two
                    2 -> Res.drawable.ic_tab_three
                    3 -> Res.drawable.ic_tab_four
                    else -> Res.drawable.ic_tab_one
                }
            ),
            contentDescription = "${LocalStringResources.current.ONBOARDING_SCREEN} $page",
            modifier = Modifier
                .fillMaxWidth()
        )
        PrimaryButton(
            text = if (page == 3) LocalStringResources.current.AUTHORIZATION_ENTER else LocalStringResources.current.BUTTON_NEXT,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            onClick = onClick
        )
    }
}