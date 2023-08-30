package com.vnstudio.talktoai.presentation.screens

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vnstudio.talktoai.R
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
fun AppContent(
    startDestination: String,
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val currentRouteState = navController.currentBackStackEntryAsState().value?.destination?.route

    val chatsState = remember { mutableStateOf<List<Chat>>(listOf()) }
    val showCreateDataDialog = remember { mutableStateOf(false) }
    val showEditDataDialog = remember { mutableStateOf(false) }
    val openedChatState = remember { mutableStateOf<Chat?>(null) }
    val deletedChatState = remember { mutableStateOf<Chat?>(null) }
    val infoMessageState = remember { mutableStateOf<InfoMessage?>(null) }
    val isChatDrawerMode = remember { mutableStateOf(true) }

    Log.e("localeTAG", "AppContent LocalConfiguration.locale ${LocalConfiguration.current.locale}")

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
                isChatDrawerMode,
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
            if (isChatDrawerMode.value) {
                MainNavHost(navController,
                    startDestination,
                    infoMessageState,
                    chatsState,
                    openedChatState,
                    showCreateDataDialog,
                    showEditDataDialog,
                    deletedChatState)
            } else {
                SettingsNavHost(navController, infoMessageState)
            }
        })
}

@Composable
fun MainNavHost(navController: NavHostController,
                startDestination: String,
                infoMessageState: MutableState<InfoMessage?>,
                chatsState: MutableState<List<Chat>>,
                openedChatState: MutableState<Chat?>,
                showCreateDataDialog: MutableState<Boolean>,
                showEditDataDialog: MutableState<Boolean>,
                deletedChatState: MutableState<Chat?>) {
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
    }
}

@Composable
fun SettingsNavHost(navController: NavHostController, infoMessageState: MutableState<InfoMessage?>) {
    NavHost(navController, startDestination = NavigationScreen.SettingsChatScreen().route,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }) {
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

@Composable
fun TopAppBarTitle(route: String?): String {
    return when (route) {
        NavigationScreen.SettingsChatScreen().route -> stringResource(id = R.string.settings_chat)
        NavigationScreen.SettingsAccountScreen().route -> stringResource(id = R.string.settings_account)
        NavigationScreen.SettingsLanguageScreen().route -> stringResource(id = R.string.settings_language)
        NavigationScreen.SettingsThemeScreen().route -> stringResource(id = R.string.settings_theme)
        NavigationScreen.SettingsPrivacyPolicyScreen().route -> stringResource(id = R.string.settings_privacy_policy)
        else -> stringResource(id = R.string.app_name)
    }
}