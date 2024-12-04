package presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.vnteam.talktoai.Constants.APP_LANG_EN
import com.vnteam.talktoai.Constants.APP_LANG_UK
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.ic_dark_mode
import com.vnteam.talktoai.ic_light_mode
import com.vnteam.talktoai.presentation.ui.resources.LocalSmallPadding
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.resources.getStringResourcesByLocale
import com.vnteam.talktoai.presentation.ui.theme.AppTheme
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.AppViewModel
import org.jetbrains.compose.resources.painterResource
import presentation.screens.main.AppContent

@Composable
fun App(appViewModel: AppViewModel) {
    val isDarkTheme = appViewModel.isDarkTheme.collectAsState()
    val language = appViewModel.language.collectAsState()
    CompositionLocalProvider(LocalStringResources provides getStringResourcesByLocale(language.value.orEmpty())) {
        isDarkTheme.value?.let {
            AppTheme(it) {
                //ScaffoldContent(appViewModel)
                AppContent()
            }
        } ?: SplashScreen()
    }
}

@Composable
fun SplashScreen() {
    // TODO implement splash screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldContent(appViewModel: AppViewModel) {
    val screenState = mutableStateOf(ScreenState())
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    LaunchedEffect(screenState.value.appMessageState.messageVisible) {
        if (screenState.value.appMessageState.messageVisible) {
            snackbarHostState.showSnackbar(
                message = screenState.value.appMessageState.messageText,
                duration = SnackbarDuration.Short,
            )
            screenState.value = screenState.value.copy(
                appMessageState = screenState.value.appMessageState.copy(messageVisible = false)
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenState.value.appBarState.appBarTitle) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    if (screenState.value.appBarState.topAppBarActionVisible) {
                        IconButton(onClick = screenState.value.appBarState.topAppBarAction) {
                            Icon(
                                tint = Color.White,
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = LocalStringResources.current.CHAT_NAME
                            )
                        }
                    }
                },
                actions = {
                    if (!screenState.value.appBarState.topAppBarActionVisible) {
                        IconButton(onClick = {
                            appViewModel.setLanguage(if (appViewModel.language.value == APP_LANG_EN) APP_LANG_UK else APP_LANG_EN)
                        }) {
                            Text(
                                if (appViewModel.language.value == APP_LANG_EN) APP_LANG_UK else APP_LANG_EN,
                                color = Color.White
                            )
                        }
                        IconButton(onClick = {
                            appViewModel.setIsDarkTheme(appViewModel.isDarkTheme.value != true)
                        }) {
                            Icon(
                                painter = painterResource(if (appViewModel.isDarkTheme.value == true) Res.drawable.ic_light_mode else Res.drawable.ic_dark_mode),
                                contentDescription = if (appViewModel.isDarkTheme.value == true) "Switch to Light Theme" else "Switch to Dark Theme",
                                tint = Color.White
                            )
                        }
                    }
                })
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    actionColor = Color.White,
                    containerColor = if (screenState.value.appMessageState.isMessageError) Color.Red else Color.Green
                )
            }
        },
        floatingActionButton = {
            if (screenState.value.floatingActionState.floatingActionButtonVisible) {
                ExtendedFloatingActionButton(
                    onClick = { screenState.value.floatingActionState.floatingActionButtonAction() },
                    content = { Text(screenState.value.floatingActionState.floatingActionButtonTitle) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = Color.White,
                    modifier = Modifier.padding(
                        horizontal = 48.dp,
                        vertical = LocalSmallPadding.current.size
                    )
                )
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                AppNavigation(navController, appViewModel.screenState)
                if (screenState.value.isProgressVisible) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    )
}