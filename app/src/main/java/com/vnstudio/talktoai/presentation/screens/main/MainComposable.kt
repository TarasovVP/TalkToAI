package com.vnstudio.talktoai.presentation.screens.main

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.screens.sealed_classes.SettingsScreen.Companion.isSettingsScreen
import com.vnstudio.talktoai.presentation.screens.sealed_classes.SettingsScreen.Companion.settingsScreenNameByRoute
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AppContent() {

    val viewModel: MainViewModel = hiltViewModel()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val currentRouteState = navController.currentBackStackEntryAsState().value?.destination?.route
    val startDestinationState = remember { mutableStateOf<String?>(null) }
    val isSettingsDrawerMode = remember { mutableStateOf<Boolean?>(null) }

    val showCreateChatDialog = remember { mutableStateOf(false) }
    val showEditChatDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    val showDeleteChatDialog = remember { mutableStateOf(false) }
    val deleteChatState = remember { mutableStateOf<Chat?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getOnBoardingSeen()
    }

    val chatsState = viewModel.chatsLiveData.observeAsState()
    LaunchedEffect(viewModel) {
        viewModel.getChats()
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
            startDestinationState.value =
                if (isSettingsDrawerMode) NavigationScreen.SettingsChatScreen().route else NavigationScreen.ChatScreen().route
            if (isSettingsDrawerMode) {
                navController.navigate(NavigationScreen.SettingsChatScreen().route)
            } else {
                if (navController.popBackStack(NavigationScreen.ChatScreen().route, false)
                        .not()
                ) navController.navigate(NavigationScreen.ChatScreen().route)
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
            when {
                currentRouteState == NavigationScreen.SettingsSignUpScreen().route -> SecondaryTopBar(
                    stringResource(id = settingsScreenNameByRoute(currentRouteState))
                ) {
                    navController.navigateUp()
                }
                isSettingsScreen(currentRouteState) || currentRouteState == NavigationScreen.ChatScreen().route -> PrimaryTopBar(
                    title = if (currentRouteState == NavigationScreen.ChatScreen().route) chatsState.value?.firstOrNull()?.name
                        ?: stringResource(id = com.vnstudio.talktoai.R.string.app_name) else stringResource(
                        id = settingsScreenNameByRoute(
                            currentRouteState
                        )
                    ),
                    onNavigationIconClick = {
                        scope.launch {
                            if (scaffoldState.drawerState.isClosed) {
                                scaffoldState.drawerState.open()
                            } else {
                                scaffoldState.drawerState.close()
                            }
                        }
                    },
                    isActionVisible = currentRouteState == NavigationScreen.ChatScreen().route && chatsState.value.orEmpty()
                        .isNotEmpty()
                ) {
                    showEditChatDialog.value = true
                }
            }
        },
        snackbarHost = { snackBarHostState ->
            AppSnackBar(snackBarHostState)
        },
        drawerGesturesEnabled = isSettingsScreen(currentRouteState) || currentRouteState == NavigationScreen.ChatScreen().route,
        drawerContent = {
            AppDrawer(
                isSettingsDrawerMode,
                currentRouteState,
                chats = chatsState,
                onCreateChatClick = {
                    showCreateChatDialog.value = true
                },
                onChatClick = { chat ->
                    viewModel.updateChats(
                        viewModel.chatsLiveData.value.orEmpty()
                            .onEach { if (it.chatId == chat.chatId) it.updated = Date().time })
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                onDeleteChatClick = { chat ->
                    showDeleteChatDialog.value = true
                    deleteChatState.value = chat
                },
            ) { route ->
                navController.navigate(route)
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        },
        content = {
            startDestinationState.value?.let { startDestination ->
                AppNavHost(navController, startDestination, infoMessageState)
            }
            ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)

            DataEditDialog(
                "Создать новый чат?",
                "Название чата",
                mutableStateOf(TextFieldValue()),
                showCreateChatDialog,
                onDismiss = {
                    showCreateChatDialog.value = false
                }) { newChatName ->
                viewModel.insertChat(Chat(name = newChatName, updated = Date().time))
                showCreateChatDialog.value = false
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }

            val currentChat = chatsState.value?.firstOrNull()
            DataEditDialog(
                "Изменить название чата?",
                "Название чата",
                mutableStateOf(TextFieldValue(currentChat?.name.orEmpty())),
                showEditChatDialog,
                onDismiss = {
                    showEditChatDialog.value = false
                }) { newChatName ->
                viewModel.chatsLiveData.value = listOf()
                currentChat?.apply {
                    name = newChatName
                }?.let { viewModel.updateChat(it) }
                showEditChatDialog.value = false
            }

            ConfirmationDialog("Удалить чат?", showDeleteChatDialog, onDismiss = {
                showDeleteChatDialog.value = false
            }) {
                deleteChatState.value?.let { viewModel.deleteChat(it) }
                showDeleteChatDialog.value = false
                deleteChatState.value = null
            }
        })
}