package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.presentation.ui.components.SplashScreen
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.resources.getStringResourcesByLocale
import com.vnteam.talktoai.presentation.ui.theme.AppTheme
import com.vnteam.talktoai.presentation.viewmodels.AppViewModel
import presentation.screens.main.AppContent

@Composable
fun App(appViewModel: AppViewModel) {
    val screenState = appViewModel.screenState.collectAsState()
    val isSplashScreenVisible = remember { mutableStateOf(true) }
    if (screenState.value?.isReadyToLaunch.isTrue() && isSplashScreenVisible.value.not()) {
        CompositionLocalProvider(LocalStringResources provides getStringResourcesByLocale(screenState.value?.language.orEmpty())) {
            AppTheme(screenState.value?.isDarkTheme.isTrue()) {
                AppContent(appViewModel)
            }
        }
    } else {
        SplashScreen {
            isSplashScreenVisible.value = false
        }
    }
}

