package com.vnstudio.talktoai.presentation.screens.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.infrastructure.Constants.ERROR_MESSAGE
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.screens.chat.ChatScreen
import com.vnstudio.talktoai.presentation.screens.onboarding.login.LoginScreen
import com.vnstudio.talktoai.presentation.screens.onboarding.onboarding.OnboardingScreen
import com.vnstudio.talktoai.presentation.screens.onboarding.signup.SignUpScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_account.SettingsAccountScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_chat.SettingsChatScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_language.SettingsLanguageScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_sign_up.SettingsSignUpScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_theme.SettingsThemeScreen
import com.vnstudio.talktoai.presentation.theme.Primary700
import kotlinx.coroutines.launch

@Composable
fun AppContent() {
    val viewModel: MainViewModel = hiltViewModel()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val currentRouteState = navController.currentBackStackEntryAsState().value?.destination?.route

    val startDestinationState = remember { mutableStateOf<String?>(null) }
    val isSettingsDrawerMode = remember { mutableStateOf<Boolean?>(null) }
    val chatsState = remember { mutableStateOf<List<Chat>>(listOf()) }
    val showCreateDataDialog = remember { mutableStateOf(false) }
    val showEditDataDialog = remember { mutableStateOf(false) }
    val openedChatState = remember { mutableStateOf<Chat?>(null) }
    val deletedChatState = remember { mutableStateOf<Chat?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getOnBoardingSeen()
    }

    val onBoardingSeenState = viewModel.onBoardingSeenLiveData.observeAsState()
    LaunchedEffect(onBoardingSeenState.value) {
        onBoardingSeenState.value?.let { isOnboardingSeen ->
            startDestinationState.value = when {
                isOnboardingSeen.not() -> NavigationScreen.OnboardingScreen().route
                viewModel.isLoggedInUser().not() -> NavigationScreen.LoginScreen().route
                isSettingsDrawerMode.value.isTrue() -> NavigationScreen.SettingsChatScreen().route
                else -> NavigationScreen.ChatScreen().route
            }
        }
    }

    LaunchedEffect(isSettingsDrawerMode.value) {
        isSettingsDrawerMode.value?.let { isSettingsDrawerMode ->
            startDestinationState.value = if (isSettingsDrawerMode) NavigationScreen.SettingsChatScreen().route else NavigationScreen.ChatScreen().route
            if (isSettingsDrawerMode) {
                navController.navigate(NavigationScreen.SettingsChatScreen().route)
            } else {
                if (navController.popBackStack(NavigationScreen.ChatScreen().route, false).not()) navController.navigate(NavigationScreen.ChatScreen().route)
            }

        }
    }

    val infoMessageState = remember { mutableStateOf<InfoMessage?>(null) }
    LaunchedEffect(infoMessageState.value) {
        infoMessageState.value?.let { infoMessage ->
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = infoMessage.message,
                    actionLabel = infoMessage.type,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (NavigationScreen.isTopBarNeeded(currentRouteState)) {
                if (currentRouteState != NavigationScreen.SettingsSignUpScreen().route) {
                    PrimaryTopBar(
                        title = if (currentRouteState == NavigationScreen.ChatScreen().route) chatsState.value.firstOrNull()?.name
                            ?: "Talk to AI" else TopAppBarTitle(currentRouteState),
                        onNavigationIconClick = {
                            scope.launch {
                                if (scaffoldState.drawerState.isClosed) {
                                    scaffoldState.drawerState.open()
                                } else {
                                    scaffoldState.drawerState.close()
                                }
                            }
                        },
                        isActionVisible = currentRouteState == NavigationScreen.ChatScreen().route && chatsState.value.isNotEmpty()
                    ) {
                        showEditDataDialog.value = true
                    }
                } else {
                    SecondaryTopBar(TopAppBarTitle(currentRouteState)) {
                        navController.navigateUp()
                    }
                }
            }
        },
        snackbarHost = { snackBarHostState ->
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    Box {
                        Snackbar(
                            modifier = Modifier.padding(8.dp),
                            backgroundColor = if (data.actionLabel == ERROR_MESSAGE) Color.Red else Primary700
                        ) {
                            Text(data.message)
                        }
                        Spacer(modifier = Modifier.fillMaxSize())
                    }
                }
            )
        },
        drawerGesturesEnabled = NavigationScreen.isTopBarNeeded(currentRouteState),
        drawerContent = {
            AppDrawer(
                isSettingsDrawerMode,
                currentRouteState,
                chats = chatsState,
                onCreateChatClick = {
                    showCreateDataDialog.value = true
                },
                onChatClick = { chat ->
                    openedChatState.value = chat
                    navController.navigate(NavigationScreen.ChatScreen().route)
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                onDeleteChatClick = { chat ->
                    deletedChatState.value = chat
                },
                infoMessageState
            ) { route ->
                navController.navigate(route)
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        },
        content = {
            startDestinationState.value?.let { startDestination ->
                NavHost(navController, startDestination = startDestination,
                    enterTransition = {
                        EnterTransition.None
                    }, exitTransition = {
                        ExitTransition.None
                    }) {
                    composable(
                        route = NavigationScreen.OnboardingScreen().route
                    ) {
                        OnboardingScreen {
                            navController.navigate(NavigationScreen.LoginScreen().route)
                        }
                    }
                    composable(
                        route = NavigationScreen.LoginScreen().route
                    ) {
                        LoginScreen(infoMessageState) { route ->
                            navController.navigate(route)
                        }
                    }
                    composable(
                        route = NavigationScreen.SignUpScreen().route
                    ) {
                        SignUpScreen(infoMessageState) { route ->
                            navController.navigate(route)
                        }
                    }
                    composable(
                        route = NavigationScreen.ChatScreen().route
                    ) {
                        ChatScreen(
                            chatsState,
                            openedChatState,
                            showCreateDataDialog,
                            showEditDataDialog,
                            deletedChatState,
                            infoMessageState
                        )
                    }
                    composable(
                        route = NavigationScreen.SettingsChatScreen().route
                    ) {
                        SettingsChatScreen(infoMessageState) {

                        }
                    }
                    composable(
                        route = NavigationScreen.SettingsAccountScreen().route
                    ) {
                        SettingsAccountScreen(infoMessageState) { route ->
                            navController.navigate(route)
                        }
                    }
                    composable(
                        route = NavigationScreen.SettingsSignUpScreen().route
                    ) {
                        SettingsSignUpScreen(infoMessageState) {

                        }
                    }
                    composable(
                        route = NavigationScreen.SettingsLanguageScreen().route
                    ) {
                        SettingsLanguageScreen(infoMessageState)
                    }
                    composable(
                        route = NavigationScreen.SettingsThemeScreen().route
                    ) {
                        SettingsThemeScreen(infoMessageState)
                    }
                    composable(
                        route = NavigationScreen.SettingsPrivacyPolicyScreen().route
                    ) {
                        SettingsPrivacyPolicyScreen()
                    }
                }
            }
        })
}