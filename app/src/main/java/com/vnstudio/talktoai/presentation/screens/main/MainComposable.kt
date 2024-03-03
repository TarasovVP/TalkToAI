package com.vnstudio.talktoai.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.vnstudio.talktoai.CommonExtensions.isNotTrue
import com.vnstudio.talktoai.CommonExtensions.isNull
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.dateToMilliseconds
import com.vnstudio.talktoai.domain.enums.AuthState
import com.vnstudio.talktoai.domain.models.ScreenState
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen.Companion.isSettingsScreen
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen.Companion.settingsScreenNameByRoute
import com.vnstudio.talktoai.getCurrentScreenRoute
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.infrastructure.Constants.DESTINATION_CHAT_SCREEN
import com.vnstudio.talktoai.presentation.components.AppDrawer
import com.vnstudio.talktoai.presentation.components.AppSnackBar
import com.vnstudio.talktoai.presentation.components.ConfirmationDialog
import com.vnstudio.talktoai.presentation.components.DataEditDialog
import com.vnstudio.talktoai.presentation.components.DeleteModeTopBar
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.MainProgress
import com.vnstudio.talktoai.presentation.components.PrimaryTopBar
import com.vnstudio.talktoai.presentation.components.ProvideAppNavigator
import com.vnstudio.talktoai.presentation.components.SecondaryTopBar
import com.vnstudio.talktoai.presentation.components.stringRes
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppContent() {

    val viewModel: MainViewModel = koinViewModel()

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val screenState = remember { mutableStateOf(ScreenState()) }
    val appNavigator = remember { mutableStateOf<Navigator?>(null) }

    val currentRouteState = remember { mutableStateOf<String?>(null) }
    val startDestinationState = remember { mutableStateOf<String?>(null) }
    val onBoardingSeenState = viewModel.onBoardingSeenLiveData.collectAsState()
    val authState = viewModel.authStateLiveData.collectAsState()
    val chatsState = viewModel.chatsLiveData.collectAsState()

    val isSettingsDrawerModeState = remember { mutableStateOf<Boolean?>(null) }
    val isMessageDeleteModeState = remember { mutableStateOf<Boolean?>(null) }

    val showCreateChatDialog = remember { mutableStateOf(false) }
    val showEditChatDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    val showDeleteChatDialog = remember { mutableStateOf(false) }
    val currentChatState = remember { mutableStateOf<Chat?>(null) }
    val deleteChatState = remember { mutableStateOf<Chat?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getOnBoardingSeen()
        viewModel.addAuthStateListener()
    }

    LaunchedEffect(onBoardingSeenState.value) {
        onBoardingSeenState.value?.let { isOnboardingSeen ->
            startDestinationState.value = when {
                isOnboardingSeen.not() -> NavigationScreen.OnboardingScreen().route
                viewModel.isLoggedInUser().not() -> NavigationScreen.LoginScreen().route
                isSettingsDrawerModeState.value.isTrue() -> NavigationScreen.SettingsChatScreen().route
                else -> NavigationScreen.ChatScreen().route
            }
        }
    }

    LaunchedEffect(authState.value) {
        authState.value?.let { authStateValue ->
            when (authStateValue) {
                AuthState.UNAUTHORISED -> viewModel.removeRemoteUserListeners()
                AuthState.AUTHORISED_ANONYMOUSLY -> viewModel.getChats()
                else -> {
                    viewModel.getChats()
                    viewModel.addRemoteChatListener()
                    viewModel.addRemoteMessageListener()
                }
            }
        }
    }

    LaunchedEffect(chatsState.value) {
        chatsState.value?.let { chatsState ->
            when {
                currentChatState.value.isNull() -> currentChatState.value = chatsState.firstOrNull()
                chatsState.contains(currentChatState.value).not() -> {
                    currentChatState.value = chatsState.firstOrNull()
                    screenState.value.nextScreenState.value =
                        "$DESTINATION_CHAT_SCREEN/${currentChatState.value?.id ?: DEFAULT_CHAT_ID}"
                }
            }
        }
    }

    LaunchedEffect(screenState.value.nextScreenState.value) {
        val navigationScreen = NavigationScreen.fromRoute(screenState.value)
        if ((navigationScreen as? NavigationScreen)?.route != (appNavigator.value?.getCurrentScreenRoute())) {
            appNavigator.value?.push(navigationScreen)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            when {
                isMessageDeleteModeState.value.isTrue() -> DeleteModeTopBar("Выбрано")
                currentRouteState.value == NavigationScreen.SettingsSignUpScreen().route -> SecondaryTopBar(
                    settingsScreenNameByRoute(currentRouteState.value, stringRes())
                ) {
                    appNavigator.value?.pop()
                }

                isSettingsScreen(currentRouteState.value) || currentRouteState.value == NavigationScreen.ChatScreen().route -> PrimaryTopBar(
                    title = if (currentRouteState.value == NavigationScreen.ChatScreen().route) currentChatState.value?.name
                        ?: stringRes().APP_NAME else settingsScreenNameByRoute(
                        currentRouteState.value,
                        stringRes()
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
                    isActionVisible = currentRouteState.value == NavigationScreen.ChatScreen().route && chatsState.value.orEmpty()
                        .isNotEmpty()
                ) {
                    showEditChatDialog.value = true
                }
            }
        },
        snackbarHost = { snackBarHostState ->
            AppSnackBar(snackBarHostState)
            LaunchedEffect(screenState.value.infoMessageState.value) {
                screenState.value.infoMessageState.value?.let { infoMessage ->
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = infoMessage.message,
                            actionLabel = infoMessage.type,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }

        },
        drawerGesturesEnabled = isSettingsScreen(currentRouteState.value) || (currentRouteState.value == NavigationScreen.ChatScreen().route && isMessageDeleteModeState.value.isNotTrue()),
        drawerContent = {
            AppDrawer(
                isSettingsDrawerModeState,
                currentRouteState.value,
                currentChatState.value?.id,
                chats = chatsState,
                onCreateChatClick = {
                    showCreateChatDialog.value = true
                },
                onChatClick = { chat ->
                    currentChatState.value = chat
                    screenState.value.nextScreenState.value =
                        "$DESTINATION_CHAT_SCREEN/${currentChatState.value?.id}"
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                onDeleteChatClick = { chat ->
                    showDeleteChatDialog.value = true
                    deleteChatState.value = chat
                },
                onSwap = { firstIndex, secondIndex ->
                    viewModel.swapChats(firstIndex, secondIndex)
                },
                onDragEnd = {
                    viewModel.updateChats(chatsState.value.orEmpty())
                }
            ) { route ->
                //TODO
                //navController.navigate(route)
                screenState.value.nextScreenState.value = route
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
            LaunchedEffect(isSettingsDrawerModeState.value) {
                isSettingsDrawerModeState.value?.let { isSettingsDrawerMode ->
                    if (isSettingsDrawerMode) {
                        viewModel.removeRemoteUserListeners()
                        startDestinationState.value =
                            NavigationScreen.SettingsChatScreen().route
                        //TODO
                        //navController.navigate(NavigationScreen.SettingsChatScreen().route)
                    } else {
                        viewModel.addRemoteChatListener()
                        viewModel.addRemoteMessageListener()
                        //TODO
                        /*if (navController.popBackStack(
                                NavigationScreen.ChatScreen().route,
                                false
                            )
                                .not()
                        ) navController.navigate(NavigationScreen.ChatScreen().route)*/
                    }
                }
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Navigator(
                    screen = NavigationScreen.fromRoute(screenState.value),
                    content = { navigator ->
                        appNavigator.value = navigator
                        ProvideAppNavigator(
                            navigator = navigator,
                            content = { SlideTransition(navigator = navigator) },
                        )
                    },
                )
                ExceptionMessageHandler(
                    screenState.value.infoMessageState,
                    viewModel.exceptionLiveData
                )

                DataEditDialog(
                    "Создать новый чат?",
                    "Название чата",
                    remember {
                        mutableStateOf(TextFieldValue())
                    },
                    showCreateChatDialog,
                    onDismiss = {
                        showCreateChatDialog.value = false
                    }) { newChatName ->
                    viewModel.insertChat(
                        Chat(
                            id = Clock.System.now().dateToMilliseconds(),
                            name = newChatName,
                            updated = Clock.System.now().dateToMilliseconds(),
                            listOrder = chatsState.value.orEmpty().size + 1
                        )
                    )
                    showCreateChatDialog.value = false
                    currentChatState.value = null
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }

                DataEditDialog(
                    "Изменить название чата?",
                    "Название чата",
                    remember {
                        mutableStateOf(TextFieldValue(currentChatState.value?.name.orEmpty()))
                    },
                    showEditChatDialog,
                    onDismiss = {
                        showEditChatDialog.value = false
                    }) { newChatName ->
                    currentChatState.value?.apply {
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

                MainProgress(screenState.value.progressVisibilityState)
            }
        })
}