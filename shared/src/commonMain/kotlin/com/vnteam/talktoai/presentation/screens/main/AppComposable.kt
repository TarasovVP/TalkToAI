package com.vnteam.talktoai.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.presentation.ui.components.CreateChatDialog
import com.vnteam.talktoai.presentation.viewmodels.chats.ChatListViewModel
import com.vnteam.talktoai.presentation.viewmodels.settings.AppViewModel
import kotlinx.coroutines.launch
import com.vnteam.talktoai.presentation.AppNavigation
import com.vnteam.talktoai.presentation.LocalScreenState
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppContent(appViewModel: AppViewModel) {

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navController = rememberNavController()
    val chatListViewModel = koinViewModel<ChatListViewModel>()

    val screenState = LocalScreenState.current
    val showRenameChatDialog = remember { mutableStateOf(false) }

    println("AppContentTAG: AppContent screenState.value: ${screenState.value}")

    LaunchedEffect(screenState.value.currentScreenRoute) {
        scope.launch { drawerState.close() }
        screenState.value.currentScreenRoute?.let { route ->
            if (route.startsWith(NavigationScreen.CHAT_DESTINATION)) {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationRoute.orEmpty()) {
                        inclusive = true
                    }
                }
            } else {
                navController.navigate(route)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = screenState.value.isLoggedInUser,
        drawerContent = {
            if (screenState.value.isLoggedInUser) {
                ModalDrawerSheet {
                    DrawerContent(screenState.value) { newScreenState ->
                        println("AppTAG DrawerContent newScreenState: $newScreenState")
                        appViewModel.updateScreenState(newScreenState)
                        scope.launch {
                            drawerState.close()
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    screenState = screenState.value,
                    onNavigationIconClick = {
                        if (screenState.value.isSecondaryScreen.isTrue()) {
                            navController.previousBackStackEntry?.destination?.route?.let { route ->
                                screenState.value = screenState.value.copy(currentScreenRoute = route)
                            }
                            navController.navigateUp()
                        } else {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                }
                            }
                        }
                    },
                    onEditChatClick = { showRenameChatDialog.value = true }
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
                AppNavigation(navController, screenState.value.startDestination)
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

    CreateChatDialog(
        currentChatName = screenState.value.currentChat?.name.orEmpty(),
        showDialog = showRenameChatDialog
    ) { newName ->
        screenState.value.currentChat?.let { chat ->
            val updatedChat = chat.copy(name = newName)
            chatListViewModel.updateChat(updatedChat)
            screenState.value = screenState.value.copy(currentChat = updatedChat)
        }
    }
}