package presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
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
import com.vnteam.talktoai.domain.enums.AuthState
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.NavigationScreen.Companion.settingsScreenNameByRoute
import com.vnteam.talktoai.presentation.ui.components.ExceptionMessageHandler
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.viewmodels.AppViewModel
import com.vnteam.talktoai.presentation.viewmodels.ChatListViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import presentation.AppNavigation
import presentation.AppSnackBar
import presentation.DeleteModeTopBar
import presentation.PrimaryTopBar
import presentation.SecondaryTopBar

@Composable
fun AppContent(appViewModel: AppViewModel) {

    val viewModel: ChatListViewModel = koinViewModel()

    // Scaffold
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackBarHostState = remember { SnackbarHostState() }

    val screenState = appViewModel.screenState.collectAsState()
    val animationResourceState = appViewModel.animationResource.collectAsState()

    println("AppContentTAG: screenState.value: ${screenState.value}")

    val navController = rememberNavController()

    val authState = viewModel.authState.collectAsState()

    val isMessageActionModeState = remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        viewModel.addAuthStateListener()
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

    LaunchedEffect(screenState.value?.infoMessageState?.value) {
        screenState.value?.infoMessageState?.value?.let { infoMessage ->
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = infoMessage.message,
                    actionLabel = infoMessage.type,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(screenState.value?.currentScreenRoute) {
        navController.navigate(
            screenState.value?.currentScreenRoute ?: NavigationScreen.ChatScreen.route
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = screenState.value?.isDrawerGesturesEnabled.isTrue(),
        drawerContent = {
            DrawerContent(screenState.value) { newScreenState, isDrawerClose ->
                appViewModel.updateScreenState(newScreenState)
                if (isDrawerClose) {
                    scope.launch {
                        drawerState.close()
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                when {
                    isMessageActionModeState.value.isTrue() -> DeleteModeTopBar(LocalStringResources.current.MESSAGE_ACTION_SELECTED)
                    screenState.value?.currentScreenRoute == NavigationScreen.SettingsSignUpScreen.route -> SecondaryTopBar(
                        settingsScreenNameByRoute(
                            screenState.value?.currentScreenRoute,
                            LocalStringResources.current
                        )
                    ) {
                        navController.popBackStack()
                    }

                    else -> PrimaryTopBar(
                        title = if (screenState.value?.isChatScreen.isTrue()
                        ) screenState.value?.currentChat?.name
                            ?: LocalStringResources.current.APP_NAME else settingsScreenNameByRoute(
                            screenState.value?.currentScreenRoute,
                            LocalStringResources.current
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
                        isActionVisible = screenState.value?.isChatScreen.isTrue() && screenState.value?.currentChat != null
                    ) {
                        //showEditChatDialog.value = true
                    }
                }
            },
            snackbarHost = {
                AppSnackBar(snackBarHostState)
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                AppNavigation(navController, mutableStateOf(screenState.value))
                ExceptionMessageHandler(
                    screenState.value?.infoMessageState,
                    viewModel.exceptionLiveData
                )

                if (screenState.value?.isProgressVisible.isTrue()) {
                    viewModel.animationUtils.MainProgressAnimation(
                        animationResourceState.value.orEmpty()
                    )
                }
            }
        }
    }
}