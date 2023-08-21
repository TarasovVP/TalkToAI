package com.vnstudio.talktoai.presentation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vnstudio.talktoai.presentation.chat.ChatScreen
import com.vnstudio.talktoai.presentation.main.MainScreen
import com.vnstudio.talktoai.presentation.settings.SettingsScreen

@Composable
fun AppContent() {
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
        MainNavGraph(showCreateDataDialog, navController)
    }

}

@Composable
fun MainNavGraph(
    showCreateDataDialog: MutableState<Boolean>,
    navController: NavHostController
) {
    NavHost(navController, startDestination = "destination_chat_screen") {
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