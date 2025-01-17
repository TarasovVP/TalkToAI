package presentation.screens.authorization.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnteam.talktoai.domain.sealed_classes.OnboardingPage
import com.vnteam.talktoai.domain.sealed_classes.OnboardingPage.Companion.getOnboardingPageDescription
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral50
import com.vnteam.talktoai.presentation.ui.theme.Primary500
import com.vnteam.talktoai.presentation.viewmodels.authorisation.OnBoardingViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen() {
    val viewModel = koinViewModel<OnBoardingViewModel>()
    val pageState = remember {
        mutableIntStateOf(0)
    }
    val onboardingPage = OnboardingPage.getOnboardingPage(pageState.value)
    OnboardingContent(onboardingPage) {
        if (pageState.value == 3) {
            viewModel.setOnBoardingSeen()
        } else {
            pageState.value++
        }
    }
}

@Composable
fun OnboardingContent(onboardingPage: OnboardingPage, onClick: () -> Unit) {
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
                .heightIn(min = with(LocalDensity.current) { 4 * 12.sp.toPx() }.dp)
                .background(color = Primary500, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = onboardingPage.getOnboardingPageDescription(LocalStringResources.current),
                textAlign = TextAlign.Center,
                color = Neutral50,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        Image(
            painter = painterResource(onboardingPage.mainImage),
            contentDescription = LocalStringResources.current.ONBOARDING_ICON,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        )
        Image(
            painter = painterResource(onboardingPage.tabImage),
            contentDescription = "${LocalStringResources.current.ONBOARDING_SCREEN} ${onboardingPage.page}",
            modifier = Modifier
                .fillMaxWidth()
        )
        PrimaryButton(
            text = if (onboardingPage.page == 3) LocalStringResources.current.AUTHORIZATION_ENTER else LocalStringResources.current.BUTTON_NEXT,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            onClick = onClick
        )
    }
}