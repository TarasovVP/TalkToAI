package presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.compose.rememberNavController
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.infrastructure.Constants.DESTINATION_CHAT_SCREEN
import com.vnstudio.talktoai.infrastructure.components.AppDrawer
import com.vnstudio.talktoai.infrastructure.components.AppSnackBar
import com.vnstudio.talktoai.infrastructure.components.DeleteModeTopBar
import com.vnstudio.talktoai.infrastructure.components.PrimaryTopBar
import com.vnstudio.talktoai.infrastructure.components.SecondaryTopBar
import com.vnteam.talktoai.CommonExtensions.isNotTrue
import com.vnteam.talktoai.CommonExtensions.isNull
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.domain.enums.AuthState
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.NavigationScreen
import com.vnteam.talktoai.presentation.NavigationScreen.Companion.isSettingsScreen
import com.vnteam.talktoai.presentation.NavigationScreen.Companion.settingsScreenNameByRoute
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.MainViewModel
import components.ConfirmationDialog
import components.DataEditDialog
import components.ExceptionMessageHandler
import components.MainProgress
import dateToMilliseconds
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.compose.viewmodel.koinViewModel
import presentation.AppNavigation
import resources.LocalStringResources


@Composable
fun AppContent() {

    val viewModel: MainViewModel = koinViewModel()

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackBarHostState = remember { SnackbarHostState() }

    val screenState = remember { mutableStateOf(ScreenState()) }

    val onBoardingSeenState = viewModel.onBoardingSeen.collectAsState()
    val authState = viewModel.authState.collectAsState()
    val chatsState = viewModel.chatsList.collectAsState()

    val isSettingsDrawerModeState = remember { mutableStateOf(false) }
    val isMessageActionModeState = remember { mutableStateOf<Boolean?>(null) }

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
            screenState.value.currentScreenState.value = when {
                isOnboardingSeen.not() -> NavigationScreen.OnboardingScreen().route
                viewModel.isLoggedInUser().not() -> NavigationScreen.LoginScreen().route
                isSettingsDrawerModeState.value.isTrue() -> NavigationScreen.SettingsChatScreen().route
                else -> NavigationScreen.ChatScreen(isMessageActionModeState = isMessageActionModeState).route
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
        chatsState.value?.let { chats ->
            when {
                currentChatState.value.isNull() -> currentChatState.value = chats.firstOrNull()
                chats.contains(currentChatState.value).not() -> {
                    currentChatState.value = chats.firstOrNull()
                    screenState.value.currentScreenState.value =
                        "$DESTINATION_CHAT_SCREEN/${currentChatState.value?.id ?: DEFAULT_CHAT_ID}"
                }
            }
        }
    }

    LaunchedEffect(screenState.value.infoMessageState.value) {
        screenState.value.infoMessageState.value?.let { infoMessage ->
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = infoMessage.message,
                    actionLabel = infoMessage.type,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(screenState.value.currentScreenState.value) {
        val navigationScreen = when {
            NavigationScreen.isChatScreen(screenState.value.currentScreenState.value) -> NavigationScreen.ChatScreen(
                currentChatState.value?.id ?: -1L,
                showCreateChatDialog,
                isMessageActionModeState,
                screenState.value
            )

            else -> NavigationScreen.fromRoute(screenState.value)
        }
        val route = screenState.value.currentScreenState.value.orEmpty()
        /*if ((navigationScreen as? NavigationScreen)?.route != (navController.currentBackStackEntry?.destination?.route)) {
            navController.navigate(route)
        }*/
    }

    val isDrawerGesturesEnabled = isSettingsScreen(navController.currentBackStackEntry?.destination?.route) ||
            (navController.currentBackStackEntry?.destination?.route == NavigationScreen.ChatScreen(
                isMessageActionModeState = isMessageActionModeState
            ).route && isMessageActionModeState.value.isNotTrue())

    LaunchedEffect(isSettingsDrawerModeState.value) {
        isSettingsDrawerModeState.value.let { isSettingsDrawerMode ->
            if (isSettingsDrawerMode) {
                viewModel.removeRemoteUserListeners()
                screenState.value.currentScreenState.value =
                    NavigationScreen.SettingsChatScreen().route
            } else {
                viewModel.addRemoteChatListener()
                viewModel.addRemoteMessageListener()
                navController.popBackStack()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isDrawerGesturesEnabled,
        drawerContent = {
            AppDrawer(
                isSettingsDrawerModeState,
                navController.currentBackStackEntry?.destination?.route,
                currentChatState.value?.id,
                chats = chatsState,
                onCreateChatClick = {
                    showCreateChatDialog.value = true
                },
                onChatClick = { chat ->
                    currentChatState.value = chat
                    screenState.value.currentScreenState.value =
                        "$DESTINATION_CHAT_SCREEN/${currentChatState.value?.id}"
                    scope.launch {
                        drawerState.close()
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
                screenState.value.currentScreenState.value = route
                scope.launch {
                    drawerState.close()
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                when {
                    isMessageActionModeState.value.isTrue() -> DeleteModeTopBar(LocalStringResources.current.MESSAGE_ACTION_SELECTED)
                    navController.currentBackStackEntry?.destination?.route == NavigationScreen.SettingsSignUpScreen().route -> SecondaryTopBar(
                        settingsScreenNameByRoute(
                            navController.currentBackStackEntry?.destination?.route,
                            com.vnteam.talktoai.presentation.StringResources
                        )
                    ) {
                        navController.popBackStack()
                    }

                    isSettingsScreen(navController.currentBackStackEntry?.destination?.route) || navController.currentBackStackEntry?.destination?.route == NavigationScreen.ChatScreen(
                        isMessageActionModeState = isMessageActionModeState
                    ).route -> PrimaryTopBar(
                        title = if (navController.currentBackStackEntry?.destination?.route == NavigationScreen.ChatScreen(
                                isMessageActionModeState = isMessageActionModeState
                            ).route
                        ) currentChatState.value?.name
                            ?: LocalStringResources.current.APP_NAME else settingsScreenNameByRoute(
                            navController.currentBackStackEntry?.destination?.route,
                            com.vnteam.talktoai.presentation.StringResources
                        ),
                        onNavigationIconClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        },
                        isActionVisible = navController.currentBackStackEntry?.destination?.route == NavigationScreen.ChatScreen(
                            isMessageActionModeState = isMessageActionModeState
                        ).route && chatsState.value.orEmpty()
                            .isNotEmpty()
                    ) {
                        showEditChatDialog.value = true
                    }
                }
            },
            snackbarHost = {
                AppSnackBar(snackBarHostState)
            }
        ) {
            Box {
                AppNavigation(navController, screenState)
                ExceptionMessageHandler(
                    screenState.value.infoMessageState,
                    viewModel.exceptionLiveData
                )

                DataEditDialog(
                    LocalStringResources.current.CHAT_CREATE_TITLE,
                    LocalStringResources.current.CHAT_NAME,
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
                            listOrder = (chatsState.value.orEmpty().size + 1).toLong()
                        )
                    )
                    showCreateChatDialog.value = false
                    currentChatState.value = null
                    scope.launch {
                        drawerState.close()
                    }
                }

                DataEditDialog(
                    LocalStringResources.current.CHAT_RENAME_TITLE,
                    LocalStringResources.current.CHAT_NAME,
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

                ConfirmationDialog(
                    LocalStringResources.current.CHAT_DELETE_TITLE,
                    showDeleteChatDialog,
                    onDismiss = {
                        showDeleteChatDialog.value = false
                    }) {
                    deleteChatState.value?.let { viewModel.deleteChat(it) }
                    showDeleteChatDialog.value = false
                    deleteChatState.value = null
                }

                MainProgress(mutableStateOf( screenState.value.isProgressVisible))
            }
        }
    }
}