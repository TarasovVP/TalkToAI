package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.presentation.ui.components.SplashScreen
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.resources.getStringResourcesByLocale
import com.vnteam.talktoai.presentation.ui.theme.AppTheme
import com.vnteam.talktoai.presentation.uimodels.screen.AppMessage
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.AppViewModel
import presentation.screens.main.AppContent

@Composable
fun App(appViewModel: AppViewModel) {
    val screenState = appViewModel.screenState.collectAsState()

    val isSplashScreenVisible = remember { mutableStateOf(true) }
    println("AppTAG: screenState.value: ${screenState.value} isSplashScreenVisible: ${isSplashScreenVisible.value}")
    if (screenState.value.isReadyToLaunch && isSplashScreenVisible.value.not()) {
        CompositionLocalProvider(
            LocalStringResources provides getStringResourcesByLocale(
                screenState.value.language.orEmpty()
            ), LocalScreenState provides mutableStateOf(screenState.value)
        ) {
            AppTheme(screenState.value.isDarkTheme.isTrue()) {
                println("AppTAG: AppContent")
                AppContent(appViewModel)
            }
        }
    } else {
        SplashScreen {
            isSplashScreenVisible.value = false
        }
    }
}

val LocalScreenState = compositionLocalOf<MutableState<ScreenState>> {
    error("No ScreenState provided")
}

@Composable
fun updateScreenState(
    isProgressVisible: Boolean = false,
    appMessage: AppMessage? = null,
    screenRoute: String? = null
) {
    val localScreenState = LocalScreenState.current
    localScreenState.value = localScreenState.value.copy(
        isProgressVisible = isProgressVisible,
        appMessage = appMessage,
        currentScreenRoute = screenRoute ?: localScreenState.value.currentScreenRoute
    )
}

