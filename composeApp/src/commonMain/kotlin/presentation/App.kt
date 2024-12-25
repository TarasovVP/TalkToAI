package presentation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.resources.getStringResourcesByLocale
import com.vnteam.talktoai.presentation.ui.theme.AppTheme
import com.vnteam.talktoai.presentation.viewmodels.AppViewModel
import presentation.screens.main.AppContent

@Composable
fun App(appViewModel: AppViewModel) {
    val screenState = appViewModel.screenState.collectAsState()
    println("AppTAG App screenState: $screenState")
    if (screenState.value?.isReadyToLaunch.isTrue()) {
        CompositionLocalProvider(LocalStringResources provides getStringResourcesByLocale(screenState.value?.language.orEmpty())) {
            AppTheme(screenState.value?.isDarkTheme.isTrue()) {
                AppContent(appViewModel)
            }
        }
    } else {
        SplashScreen()
    }
}

@Composable
fun SplashScreen() {
    // TODO implement splash screen
    Text(text = "Splash Screen", modifier = Modifier.fillMaxHeight())
}