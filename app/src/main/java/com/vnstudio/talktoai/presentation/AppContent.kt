package com.vnstudio.talktoai.presentation

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.presentation.chat.ChatScreen
import com.vnstudio.talktoai.presentation.chat.ChatViewModel
import com.vnstudio.talktoai.presentation.components.AppDrawer
import com.vnstudio.talktoai.presentation.components.PrimaryTopBar
import com.vnstudio.talktoai.presentation.components.SecondaryTopBar
import com.vnstudio.talktoai.presentation.onboarding.login.LoginScreen
import com.vnstudio.talktoai.presentation.onboarding.onboarding.OnboardingScreen
import com.vnstudio.talktoai.presentation.onboarding.signup.SignUpScreen
import com.vnstudio.talktoai.presentation.settings.settings_account.SettingsAccountScreen
import com.vnstudio.talktoai.presentation.settings.settings_chat.SettingsChatScreen
import com.vnstudio.talktoai.presentation.settings.settings_language.SettingsLanguageScreen
import com.vnstudio.talktoai.presentation.settings.settings_list.SettingsListScreen
import com.vnstudio.talktoai.presentation.settings.settings_privacy_policy.SettingsPrivacyPolicyScreen
import com.vnstudio.talktoai.presentation.settings.settings_sign_up.SettingsSignUpScreen
import com.vnstudio.talktoai.presentation.settings.settings_theme.SettingsThemeScreen
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AppContent(
    startDestination: String,
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val currentRouteState = navController.currentBackStackEntryAsState().value?.destination?.route

    val viewModel: ChatViewModel = hiltViewModel()
    val showCreateDataDialog = remember { mutableStateOf(false) }
    val showEditDataDialog = remember { mutableStateOf(false) }
    val deletedChat = remember { mutableStateOf<Chat?>(null) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (currentRouteState == "destination_chat_screen" || currentRouteState == "destination_settings_list_screen") {
                PrimaryTopBar(
                    title = getTopAppBarTitle(currentRouteState),
                    onNavigationIconClick = {
                        scope.launch {
                            if (scaffoldState.drawerState.isClosed) {
                                scaffoldState.drawerState.open()
                            } else {
                                scaffoldState.drawerState.close()
                            }
                        }
                    },
                    isActionVisible = currentRouteState == "destination_chat_screen"
                ) {
                    showEditDataDialog.value = true
                }
            } else if (currentRouteState == "destination_settings_chat_screen" || currentRouteState == "destination_settings_account_screen") {
                SecondaryTopBar(getTopAppBarTitle(currentRouteState)) {
                    navController.navigateUp()
                }
            }
        }, drawerContent = {
            AppDrawer(
                chats = listOf(),
                onCreateChatClick = {
                    showCreateDataDialog.value = true
                },
                onChatClick = { chat ->
                    navController.navigate("destination_chat_screen")
                    viewModel.updateChats(viewModel.chatsLiveData.value.orEmpty().onEach { if (it.chatId == chat.chatId) it.updated = Date().time })
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                onDeleteChatClick = { chat ->
                    deletedChat.value = chat
                },
                onSettingsClick = {
                    navController.navigate("destination_settings_list_screen")
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        }, content = {
            NavHost(navController, startDestination = startDestination) {
                composable(
                    route = "destination_onboarding_screen"
                ) {
                    OnboardingScreen {
                        navController.navigate("destination_login_screen")
                    }
                }
                composable(
                    route = "destination_login_screen"
                ) {
                    LoginScreen { destination ->
                        navController.navigate(destination)
                    }
                }
                composable(
                    route = "destination_sign_up_screen"
                ) {
                    SignUpScreen { destination ->
                        navController.navigate(destination)
                    }
                }
                composable(
                    route = "destination_chat_screen"
                ) {
                    ChatScreen(viewModel, showCreateDataDialog, showEditDataDialog, deletedChat)
                }
                composable(
                    route = "destination_settings_list_screen"
                ) {
                    SettingsListScreen { destination ->
                        navController.navigate(destination)
                    }
                }
                composable(
                    route = "destination_settings_chat_screen"
                ) {
                    SettingsChatScreen {

                    }
                }
                composable(
                    route = "destination_settings_account_screen"
                ) {
                    SettingsAccountScreen {

                    }
                }
                composable(
                    route = "destination_settings_sign_up_screen"
                ) {
                    SettingsSignUpScreen {

                    }
                }
                composable(
                    route = "destination_settings_language_screen"
                ) {
                    SettingsLanguageScreen {

                    }
                }
                composable(
                    route = "destination_settings_theme_screen"
                ) {
                    SettingsThemeScreen {

                    }
                }
                composable(
                    route = "destination_settings_privacy_policy_screen"
                ) {
                    SettingsPrivacyPolicyScreen {

                    }
                }
            }
        })
}

fun getTopAppBarTitle(route: String?): String {
    return when (route) {
        "destination_chat_screen" -> "Chat"
        "destination_settings_list_screen" -> "Settings"
        // Add more cases for other screens
        else -> "My App"
    }
}