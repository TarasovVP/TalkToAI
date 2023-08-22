package com.vnstudio.talktoai.presentation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vnstudio.talktoai.presentation.chat.ChatScreen
import com.vnstudio.talktoai.presentation.main.MainScreen
import com.vnstudio.talktoai.presentation.onboarding.login.LoginScreen
import com.vnstudio.talktoai.presentation.onboarding.onboarding.OnboardingScreen
import com.vnstudio.talktoai.presentation.onboarding.SignUpScreen
import com.vnstudio.talktoai.presentation.settings.SettingsScreen

@Composable
fun AppContent(startDestination: String) {
    val navController = rememberNavController()
    val showCreateDataDialog = mutableStateOf(false)
    MainScreen (showCreateDataDialog,
        onChatClicked = {
            navController.navigate("destination_chat_screen")
        },
        onSettingsClicked = {
            navController.navigate("destination_settings_screen")
        }
    ) {
        AppNavGraph(showCreateDataDialog, navController, startDestination)
    }

}

@Composable
fun AppNavGraph(
    showCreateDataDialog: MutableState<Boolean>,
    navController: NavHostController,
    startDestination: String
) {
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
            LoginScreen {
                navController.navigate("destination_chat_screen")
            }
        }
        composable(
            route = "destination_sign_up_screen"
        ) {
            SignUpScreen {

            }
        }
        composable(
            route = "destination_chat_screen"
        ) {
            ChatScreen(showCreateDataDialog)
        }
        composable(
            route = "destination_settings_screen"
        ) {
            SettingsScreen {

            }
        }
    }
}