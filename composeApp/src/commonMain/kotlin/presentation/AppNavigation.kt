package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vnteam.talktoai.presentation.NavigationScreen
import resources.LocalStringResources
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import presentation.screens.ChatContent
import presentation.screens.main.AppContent

@Composable
fun AppNavigation(navController: NavHostController, screenState: MutableState<ScreenState>) {
    NavHost(navController = navController, startDestination = NavigationScreen.CHAT_SCREEN) {
        composable(NavigationScreen.CHAT_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = LocalStringResources.current.APP_NAME,
                    topAppBarActionVisible = false,
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = true,
                    floatingActionButtonTitle = "LocalStringResources.current.ADD",
                    floatingActionButtonAction = {
                        navController.navigate("${NavigationScreen.CHAT_SCREEN}-1")
                    }
                )
            )
            AppContent()
        //ChatContent(1L, mutableStateOf(false), mutableStateOf(false), ScreenState())
        }
        composable("${NavigationScreen.SETTINGS_CHAT_SCREEN}{demoObjectId}", arguments = listOf(navArgument("demoObjectId") {
            type = NavType.StringType
            defaultValue = ""
            nullable = true
        })) { backStackEntry ->
            val demoObjectId = backStackEntry.arguments?.getString("demoObjectId").takeIf { it?.isNotEmpty() == true && it != "-1"} ?: ""
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = if (demoObjectId.isNotEmpty()) "LocalStringResources.current.EDIT" else "LocalStringResources.current.CREATE",
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                floatingActionButtonVisible = false
            ))

        }
    }
}