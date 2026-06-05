package com.vnteam.talktoai.presentation.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.presentation.AppNavigation
import com.vnteam.talktoai.presentation.LocalScreenState
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.screens.chat.ChatSettingsBottomSheet
import com.vnteam.talktoai.presentation.viewmodels.settings.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun AppContent(appViewModel: AppViewModel) {

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navController = rememberNavController()

    val screenState = LocalScreenState.current
    val showChatSettingsSheet = remember { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    println("AppContentTAG: AppContent screenState.value: ${screenState.value}")

    val actualRoute = navBackStackEntry?.destination?.route

    val isOnSecondaryScreen = NavigationScreen.isSettingsScreen(actualRoute) ||
            actualRoute == NavigationScreen.SettingsSignUpScreen.route ||
            actualRoute == NavigationScreen.SettingsLoginScreen.route

    BackHandler(enabled = isOnSecondaryScreen) {
        navController.navigateUp()
        screenState.value = screenState.value.copy(currentScreenRoute = null)
    }

    LaunchedEffect(actualRoute) {
        if (drawerState.isOpen) {
            drawerState.close()
        }
        if (NavigationScreen.isSettingsScreen(screenState.value.currentScreenRoute) &&
            !NavigationScreen.isSettingsScreen(actualRoute)) {
            screenState.value = screenState.value.copy(currentScreenRoute = null)
        }
    }

    val unauthorizedRoute by appViewModel.unauthorizedRoute.collectAsState()
    LaunchedEffect(unauthorizedRoute) {
        unauthorizedRoute?.let { route ->
            screenState.value = screenState.value.copy(currentScreenRoute = route)
            appViewModel.unauthorizedRoute.value = null
        }
    }

    LaunchedEffect(screenState.value.currentScreenRoute) {
        if (drawerState.isOpen) scope.launch { drawerState.close() }
        screenState.value.currentScreenRoute?.let { route ->
            when {
                route == NavigationScreen.POP_BACK -> navController.navigateUp()
                route == NavigationScreen.LOGIN_SCREEN -> navController.navigate(route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
                route.startsWith(NavigationScreen.CHAT_DESTINATION) -> navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationRoute.orEmpty()) {
                        inclusive = true
                    }
                }
                else -> navController.navigate(route)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = screenState.value.isLoggedInUser &&
                actualRoute?.startsWith(NavigationScreen.CHAT_DESTINATION) == true &&
                !screenState.value.isSecondaryScreen,
        drawerContent = {
            ModalDrawerSheet {
                if (screenState.value.isLoggedInUser) {
                    DrawerContent(
                        screenState = screenState.value.copy(currentScreenRoute = actualRoute ?: screenState.value.currentScreenRoute),
                        onScreenStateUpdate = { newScreenState ->
                            screenState.value = newScreenState
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        onSettingsClick = {
                            screenState.value = screenState.value.copy(
                                currentScreenRoute = NavigationScreen.SETTINGS_LIST_SCREEN
                            )
                        }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    screenState = screenState.value.copy(currentScreenRoute = actualRoute ?: screenState.value.currentScreenRoute),
                    onNavigationIconClick = {
                        if (isOnSecondaryScreen) {
                            navController.navigateUp()
                            screenState.value = screenState.value.copy(currentScreenRoute = null)
                        } else {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                }
                            }
                        }
                    },
                    onEditChatClick = { showChatSettingsSheet.value = true },
                )
            },
            snackbarHost = {
                AppSnackBar(screenState.value, scope)
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                val stableStartDestination = remember { screenState.value.startDestination }
                AppNavigation(navController, stableStartDestination)
                if (screenState.value.isProgressVisible) {
                    val animationResourceState = appViewModel.animationResource.collectAsState()
                    println("AppTAG AppNavigation screenState.value?.isProgressVisible.isTrue(): ${screenState.value.isProgressVisible.isTrue()}")
                    appViewModel.animationUtils.MainProgressAnimation(
                        animationResourceState.value.orEmpty()
                    )
                }
            }
        }
    }

    if (showChatSettingsSheet.value) {
        screenState.value.currentChat?.let { chat ->
            ChatSettingsBottomSheet(
                chat = chat,
                onDismiss = { showChatSettingsSheet.value = false },
                onChatUpdated = { updatedChat ->
                    screenState.value = screenState.value.copy(currentChat = updatedChat)
                    showChatSettingsSheet.value = false
                }
            )
        }
    }
}